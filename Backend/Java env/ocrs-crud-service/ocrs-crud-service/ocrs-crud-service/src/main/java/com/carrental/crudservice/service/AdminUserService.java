package com.carrental.crudservice.service;

import com.carrental.crudservice.dto.UserResponse;
import com.carrental.crudservice.entity.User;
import com.carrental.crudservice.entity.enums.UserStatus;
import com.carrental.crudservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .address(user.getAddress())
                .adharCard(user.getAdharCard())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
