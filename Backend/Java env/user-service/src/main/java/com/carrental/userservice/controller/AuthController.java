package com.carrental.userservice.controller;

import com.carrental.userservice.dto.request.CustomerRegisterRequest;
import com.carrental.userservice.dto.request.LoginRequest;
import com.carrental.userservice.dto.request.OwnerRegisterRequest;
import com.carrental.userservice.dto.response.AuthResponse;
import com.carrental.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register/customer
    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponse> registerCustomer(@Valid @RequestBody CustomerRegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerCustomer(req));
    }

    // POST /api/auth/register/owner
    @PostMapping("/register/owner")
    public ResponseEntity<AuthResponse> registerOwner(@Valid @RequestBody OwnerRegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerOwner(req));
    }

    // POST /api/auth/login
    // Body: { "email": "...", "password": "..." }
    // One endpoint for every role — the response's "role" field tells the
    // frontend which dashboard to redirect to.
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
