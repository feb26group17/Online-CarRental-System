package com.carrental.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Single login payload used for EVERY role. There is no role selector
 * anymore — the backend looks the user up by email in the master `users`
 * table and reads the role from there, then the frontend redirects to the
 * matching dashboard based on the role that comes back in AuthResponse.
 */
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
