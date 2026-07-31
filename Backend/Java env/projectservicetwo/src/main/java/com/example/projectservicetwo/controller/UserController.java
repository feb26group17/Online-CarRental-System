package com.example.projectservicetwo.controller;

import com.example.projectservicetwo.dto.UpdateUserRoleDTO;
import com.example.projectservicetwo.dto.UpdateUserStatusDTO;
import com.example.projectservicetwo.dto.UserResponseDTO;
import com.example.projectservicetwo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // GET /users/role/{role}
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    // GET /users/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(userService.getUsersByStatus(status));
    }

    // PUT /users/{id}/role
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateUserRole(
            @PathVariable Integer id,
            @RequestBody UpdateUserRoleDTO roleDTO) {
        return ResponseEntity.ok(userService.updateUserRole(id, roleDTO));
    }

    // PUT /users/{id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> updateUserStatus(
            @PathVariable Integer id,
            @RequestBody UpdateUserStatusDTO statusDTO) {
        return ResponseEntity.ok(userService.updateUserStatus(id, statusDTO));
    }

    // DELETE /users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User with ID " + id + " has been deleted successfully.");
    }
}