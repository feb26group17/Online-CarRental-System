package com.example.projectservicetwo.service;

import com.example.projectservicetwo.dto.UpdateUserRoleDTO;
import com.example.projectservicetwo.dto.UpdateUserStatusDTO;
import com.example.projectservicetwo.dto.UserResponseDTO;
import com.example.projectservicetwo.entity.User;
import com.example.projectservicetwo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get user by ID
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToDTO(user);
    }

    // Get users by role
    public List<UserResponseDTO> getUsersByRole(String role) {
        return userRepository.findByRoleIgnoreCase(role)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get users by status
    public List<UserResponseDTO> getUsersByStatus(String status) {
        return userRepository.findByStatusIgnoreCase(status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Update user role
    public UserResponseDTO updateUserRole(Integer id, UpdateUserRoleDTO roleDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setRole(roleDTO.getRole().toUpperCase());
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    // Update user status (ACTIVE / BLOCKED)
    public UserResponseDTO updateUserStatus(Integer id, UpdateUserStatusDTO statusDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setStatus(statusDTO.getStatus().toUpperCase());
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    // Delete user permanently
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Helper method to convert User entity to UserResponseDTO
    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getAddress(),
                user.getStatus(),
                user.getAdharCard(),
                user.getCreatedAt()
        );
    }
}