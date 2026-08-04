package com.carrental.crudservice.service;

import com.carrental.crudservice.dto.UserResponse;
import com.carrental.crudservice.entity.Customer;
import com.carrental.crudservice.entity.User;
import com.carrental.crudservice.entity.enums.Role;
import com.carrental.crudservice.entity.enums.UserStatus;
import com.carrental.crudservice.repository.CustomerRepository;
import com.carrental.crudservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public AdminUserService(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public UserResponse updateUserStatus(Integer id, String statusStr) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserStatus status = UserStatus.fromDbValue(statusStr);
        user.setStatus(status);

        User updated = userRepository.save(user);
        return mapToResponse(updated);
    }

    public UserResponse getUserProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return mapToResponse(user);
    }

    public UserResponse updateUserProfile(Integer userId, com.carrental.crudservice.dto.UserProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName().trim());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone().trim());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress().trim());
        }
        if (request.getAdharCard() != null) {
            user.setAdharCard(request.getAdharCard().trim());
        }

        User updated = userRepository.save(user);

        if (updated.getRole() == Role.CUSTOMER && request.getDrivingLicense() != null) {
            Customer customer = customerRepository.findByUserId(userId)
                    .orElseGet(() -> Customer.builder().userId(userId).build());
            customer.setDrivingLicense(request.getDrivingLicense().trim());
            customerRepository.save(customer);
        }

        return mapToResponse(updated);
    }

    private UserResponse mapToResponse(User user) {
        String drivingLicense = null;
        if (user.getRole() == Role.CUSTOMER) {
            Optional<Customer> customerOpt = customerRepository.findByUserId(user.getId());
            if (customerOpt.isPresent()) {
                drivingLicense = customerOpt.get().getDrivingLicense();
            }
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .address(user.getAddress())
                .adharCard(user.getAdharCard())
                .drivingLicense(drivingLicense)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
