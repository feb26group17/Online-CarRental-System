package com.carrental.userservice.entity.enums;

/**
 * Maps to users.status enum('active','pending_admin','blocked').
 * This is the single account-level gate checked on every login, regardless
 * of role — ACTIVE required to proceed; PENDING_ADMIN (new car owners
 * awaiting approval) and BLOCKED are both rejected.
 */
public enum UserStatus {
    ACTIVE,
    PENDING_ADMIN,
    BLOCKED
}
