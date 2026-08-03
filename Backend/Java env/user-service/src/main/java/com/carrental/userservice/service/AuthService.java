package com.carrental.userservice.service;

import com.carrental.userservice.dto.request.CustomerRegisterRequest;
import com.carrental.userservice.dto.request.LoginRequest;
import com.carrental.userservice.dto.request.OwnerRegisterRequest;
import com.carrental.userservice.dto.response.AuthResponse;
import com.carrental.userservice.entity.Customer;
import com.carrental.userservice.entity.User;
import com.carrental.userservice.entity.enums.Role;
import com.carrental.userservice.entity.enums.UserStatus;
import com.carrental.userservice.exception.AccountBlockedException;
import com.carrental.userservice.exception.DuplicateEmailException;
import com.carrental.userservice.exception.InvalidCredentialsException;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ============================================
    // REGISTER — Customer
    // Creates the master `users` row (role=CUSTOMER, status=ACTIVE, holding
    // name/phone/address/adhar_card) plus a linked `customer` profile row
    // (just driving_license) in the same transaction.
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
                .address(req.getAddress())
                .adharCard(req.getAdharCard())
                .password(encodedPassword)
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build());

        Customer customer = customerRepository.save(Customer.builder()
                .userId(user.getId())
                .drivingLicense(req.getDrivingLicense())
                .build());

        return AuthResponse.builder()
                .id(customer.getCustomerId())
                .userId(user.getId())
                .fullName(user.getName())
                .email(user.getEmail())
                .role(Role.CUSTOMER)
                .message("Registration successful. Please login to continue.")
                .build();
    }

    // ============================================
    // REGISTER — Car Owner
    // There is no separate owner profile table anymore and no admin
    // approval step — an owner is just a `users` row with role=OWNER,
    // status=ACTIVE, active immediately like a customer.
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
                .address(req.getAddress())
                .adharCard(req.getAdharCard())
                .password(encodedPassword)
                .role(Role.OWNER)
                .status(UserStatus.ACTIVE)
                .build());

        return AuthResponse.builder()
                .id(user.getId())
                .userId(user.getId())
                .fullName(user.getName())
                .email(user.getEmail())
                .role(Role.OWNER)
                .message("Registration successful. Please login to continue.")
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
        // No separate owner table anymore — id and userId are the same,
        // same pattern as admin below.
        String token = jwtUtil.generateToken(user.getId(), user.getId(), user.getEmail(), Role.OWNER);

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
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
