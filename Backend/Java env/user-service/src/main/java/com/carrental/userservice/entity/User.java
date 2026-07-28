package com.carrental.userservice.entity;

import com.carrental.userservice.entity.converter.RoleConverter;
import com.carrental.userservice.entity.converter.UserStatusConverter;
import com.carrental.userservice.entity.enums.Role;
import com.carrental.userservice.entity.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Maps to the new master `users` table. This is now the SINGLE source of
 * truth for authentication — email, password, role and status all live
 * here for every role, including admin (there is no separate admin table
 * anymore; the seeded admin is just a row in this table with role='admin').
 *
 * `customer` and `car_owner` are now thin profile-extension tables linked
 * back here via their own `user_id` column.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String phone;

    /** BCrypt hash, never the raw password. */
    @Column(nullable = false, length = 255)
    private String password;

    @Convert(converter = RoleConverter.class)
    @Column(nullable = false, columnDefinition = "ENUM('customer','owner','admin')")
    private Role role;

    @Convert(converter = UserStatusConverter.class)
    @Column(nullable = false, columnDefinition = "ENUM('active','pending_admin','blocked')")
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
