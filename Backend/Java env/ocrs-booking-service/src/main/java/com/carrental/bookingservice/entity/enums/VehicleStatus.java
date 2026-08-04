package com.carrental.bookingservice.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VehicleStatus {
    Available("Available"),
    Booked("Booked"),
    Maintenance("Maintenance");

    private final String displayName;

    VehicleStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
