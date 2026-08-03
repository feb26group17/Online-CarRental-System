package com.carrental.crudservice.entity.enums;

/**
 * Mirrors user-service's UserStatus enum. Maps to users.status
 * ENUM('active','blocked').
 */
public enum UserStatus {
    ACTIVE,
    BLOCKED;

    public static UserStatus fromDbValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("status is required");
        }
        return UserStatus.valueOf(value.trim().toUpperCase());
    }
}
