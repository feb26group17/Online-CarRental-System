package com.carrental.userservice.dto.response;

import com.carrental.userservice.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// NON_NULL: registration doesn't set `token`, so it's omitted from the
// JSON entirely instead of showing up as "token": null.
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Integer id;       // role-scoped id: customer_id / owner_id / users.id for admin
    private Integer userId;   // users.id — the master identity id, always present
    private String fullName;
    private String email;
    private Role role;
    private String message;
}
