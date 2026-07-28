package com.carrental.userservice.entity.enums;

/**
 * Maps to car_owner.status enum('Pending','Approved','Rejected').
 * A car owner cannot log in unless status is APPROVED.
 */
public enum OwnerStatus {
    Pending,
    Approved,
    Rejected
}
