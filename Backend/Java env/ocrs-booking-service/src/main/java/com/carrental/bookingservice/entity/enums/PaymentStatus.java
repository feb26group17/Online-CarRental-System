package com.carrental.bookingservice.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
    Pending("Pending"),
    Paid("Paid"),
    Failed("Failed"),
    Refunded("Refunded");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
