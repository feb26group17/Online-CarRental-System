package com.carrental.crudservice.controller;

import com.carrental.crudservice.dto.StatusUpdateRequest;
import com.carrental.crudservice.dto.UserResponse;
import com.carrental.crudservice.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserStatus(@PathVariable Integer id,
                                                         @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(adminUserService.updateUserStatus(id, request.getStatus()));
    }
}
