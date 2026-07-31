package com.example.projectservicetwo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectservicetwo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	// Find user by email (used in login/JWT)
    Optional<User> findByEmail(String email);

    // Get users by role
    List<User> findByRole(String role);

    // Get users by status
    List<User> findByStatus(String status);

    // Check email already exists
    boolean existsByEmail(String email);

    // Check Adhar already exists
    boolean existsByAdharCard(String adharCard);
    List<User> findByRoleIgnoreCase(String role);

    List<User> findByStatusIgnoreCase(String status);
}