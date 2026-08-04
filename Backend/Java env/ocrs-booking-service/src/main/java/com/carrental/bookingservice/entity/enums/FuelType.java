package com.carrental.bookingservice.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FuelType {
    Diesel("Diesel"),
    Petrol("Petrol"),
    CNG("CNG"),
    Battery("Battery");

    private final String displayName;

    FuelType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
