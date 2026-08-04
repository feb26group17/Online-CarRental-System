package com.carrental.bookingservice.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    UPI("UPI"),
    CreditCard("Credit Card"),
    DebitCard("Debit Card"),
    NetBanking("Net Banking"),
    Cash("Cash");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.displayName.equalsIgnoreCase(value) || method.name().equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + value);
    }
}
