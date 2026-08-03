package com.carrental.crudservice.entity.enums;

/**
 * Mirrors user-service's Role enum. Embedded as a claim in the JWT issued
 * by user-service; this copy lets crud-service map users.role for the
 * admin "list users" view without calling back to user-service.
 */
public enum Role {
    CUSTOMER,
    OWNER,
    ADMIN
}
