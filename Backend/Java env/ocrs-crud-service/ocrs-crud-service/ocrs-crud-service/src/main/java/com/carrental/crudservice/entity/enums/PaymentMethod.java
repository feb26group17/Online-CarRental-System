package com.carrental.crudservice.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    UPI("UPI"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    NET_BANKING("Net Banking"),
    CASH("Cash");

    private final String dbValue;

    PaymentMethod(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    @JsonCreator
    public static PaymentMethod fromString(String value) {
        if (value == null) return null;
        for (PaymentMethod pm : PaymentMethod.values()) {
            if (pm.dbValue.equalsIgnoreCase(value) || pm.name().equalsIgnoreCase(value) || pm.dbValue.replace(" ", "").equalsIgnoreCase(value.replace(" ", ""))) {
                return pm;
            }
        }
        return UPI;
    }
}
