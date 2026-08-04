package com.carrental.bookingservice.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BookingStatus {
    Pending("Pending"),
    Confirmed("Confirmed"),
    Cancelled("Cancelled"),
    Completed("Completed");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
