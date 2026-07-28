package com.carrental.userservice.entity.enums;

/**
 * Identity role. Drives which table a login/register request is routed to,
 * and is embedded as a claim in the issued JWT so downstream services
 * (booking, vehicle, payment) can authorize without calling back here.
 */
public enum Role {
    CUSTOMER,
    OWNER,
    ADMIN
}
