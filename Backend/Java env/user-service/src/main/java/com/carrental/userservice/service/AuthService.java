package com.carrental.userservice.service;

import com.carrental.userservice.dto.request.CustomerRegisterRequest;
import com.carrental.userservice.dto.request.LoginRequest;
import com.carrental.userservice.dto.request.OwnerRegisterRequest;
import com.carrental.userservice.dto.response.AuthResponse;
import com.carrental.userservice.entity.CarOwner;
import com.carrental.userservice.entity.Customer;
import com.carrental.userservice.entity.User;
import com.carrental.userservice.entity.enums.OwnerStatus;
import com.carrental.userservice.entity.enums.Role;
import com.carrental.userservice.entity.enums.UserStatus;
import com.carrental.userservice.exception.AccountBlockedException;
import com.carrental.userservice.exception.AccountNotApprovedException;
import com.carrental.userservice.exception.DuplicateEmailException;
import com.carrental.userservice.exception.InvalidCredentialsException;
import com.carrental.userservice.repository.CarOwnerRepository;
import com.carrental.userservice.repository.CustomerRepository;
import com.carrental.userservice.repository.UserRepository;
import com.carrental.userservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CarOwnerRepository carOwnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            CustomerRepository customerRepository,
            CarOwnerRepository carOwnerRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.carOwnerRepository = carOwnerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ============================================
    // REGISTER — Customer
    // Creates the master `users` row (role=CUSTOMER, status=ACTIVE) plus a
    // linked `customer` profile row in the same transaction.
    // No token is issued here — the user is sent to /login afterwards,
    // same as the owner registration flow.
    // ============================================
    @Transactional
    public AuthResponse registerCustomer(CustomerRegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = userRepository.save(User.builder()
                .name(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(encodedPassword)
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build());

        Customer customer = customerRepository.save(Customer.builder()
                .userId(user.getId())
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .address(req.getAddress())
                .drivingLicense(req.getDrivingLicense())
                .password(encodedPassword)
                .build());

        return AuthResponse.builder()
                .id(customer.getCustomerId())
                .userId(user.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .role(Role.CUSTOMER)
                .message("Registration successful. Please login to continue.")
                .build();
    }

    // ============================================
    // REGISTER — Car Owner
    // users.status = PENDING_ADMIN and car_owner.status = Pending, kept in
    // sync. No JWT returned — the account needs admin approval first.
    // ============================================
    @Transactional
    public AuthResponse registerOwner(OwnerRegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateEmailException("Email already registered");
        }

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        User user = userRepository.save(User.builder()
                .name(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(encodedPassword)
                .role(Role.OWNER)
                .status(UserStatus.PENDING_ADMIN)
                .build());

        CarOwner owner = carOwnerRepository.save(CarOwner.builder()
                .userId(user.getId())
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .address(req.getAddress())
                .drivingLicense(req.getDrivingLicense())
                .password(encodedPassword)
                .status(OwnerStatus.Pending)
                .build());

        return AuthResponse.builder()
                .id(owner.getOwnerId())
                .userId(user.getId())
                .fullName(owner.getFullName())
                .email(owner.getEmail())
                .role(Role.OWNER)
                .message("Registration successful. Your account is pending admin approval.")
                .build();
    }

    // ============================================
    // LOGIN — ONE endpoint, no role selector.
    // Looks the user up in `users` by email, verifies the password and the
    // account status, then dispatches on `user.role` to load the matching
    // profile. The `role` in the response is what the frontend uses to
    // redirect to /customer, /owner or /admin dashboard.
    // ============================================
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new AccountBlockedException("Your account has been blocked. Contact support.");
        }
        if (user.getStatus() == UserStatus.PENDING_ADMIN) {
            throw new AccountNotApprovedException("Your account is pending admin approval.");
        }

        return switch (user.getRole()) {
            case CUSTOMER -> loginCustomer(user);
            case OWNER -> loginOwner(user);
            case ADMIN -> loginAdmin(user);
        };
    }

    private AuthResponse loginCustomer(User user) {
        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new InvalidCredentialsException("Customer profile not found"));

        String token = jwtUtil.generateToken(customer.getCustomerId(), user.getId(), user.getEmail(), Role.CUSTOMER);

        return AuthResponse.builder()
                .token(token)
                .id(customer.getCustomerId())
                .userId(user.getId())
                .fullName(user.getName())
                .email(user.getEmail())
                .role(Role.CUSTOMER)
                .message("Login successful")
                .build();
    }

    private AuthResponse loginOwner(User user) {
        CarOwner owner = carOwnerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new InvalidCredentialsException("Car owner profile not found"));

        // Defensive — users.status should already have caught Pending/Rejected,
        // the two are kept in sync, but this guards against drift between them.
        if (owner.getStatus() == OwnerStatus.Rejected) {
            throw new AccountNotApprovedException("Your account application was rejected. Contact support.");
        }
        if (owner.getStatus() == OwnerStatus.Pending) {
            throw new AccountNotApprovedException("Your account is pending admin approval.");
        }

        String token = jwtUtil.generateToken(owner.getOwnerId(), user.getId(), user.getEmail(), Role.OWNER);

        return AuthResponse.builder()
                .token(token)
                .id(owner.getOwnerId())
                .userId(user.getId())
                .fullName(user.getName())
                .email(user.getEmail())
                .role(Role.OWNER)
                .message("Login successful")
                .build();
    }

    private AuthResponse loginAdmin(User user) {
        // No separate admin table anymore — the seeded admin is just a
        // `users` row with role='admin', so id and userId are the same.
        String token = jwtUtil.generateToken(user.getId(), user.getId(), user.getEmail(), Role.ADMIN);

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .userId(user.getId())
                .fullName(user.getName())
                .email(user.getEmail())
                .role(Role.ADMIN)
                .message("Login successful")
                .build();
    }
}
