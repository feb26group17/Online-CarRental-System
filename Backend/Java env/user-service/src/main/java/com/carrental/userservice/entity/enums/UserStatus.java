package com.carrental.userservice.entity.enums;

/**
 * Maps to users.status enum('active','blocked').
 * This is the single account-level gate checked on every login, regardless
 * of role — ACTIVE required to proceed; BLOCKED is rejected. There is no
 * approval step for new owners anymore — every role goes ACTIVE on
 * registration.
 */
public enum UserStatus {
    ACTIVE,
    BLOCKED
}
