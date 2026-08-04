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
 * `customer` is now a thin profile-extension table linked back here via
 * its own `user_id` column. There is no separate owner profile table —
 * an owner is simply a `users` row with role='owner'; `address` and
 * `adhar_card` live directly on `users` for every role.
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

    @Column(columnDefinition = "TEXT")
    private String address;

    @Convert(converter = UserStatusConverter.class)
    @Column(nullable = false, columnDefinition = "ENUM('active','blocked')")
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "adhar_card", unique = true, length = 20)
    private String adharCard;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
