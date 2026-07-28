package com.carrental.userservice.entity.converter;

import com.carrental.userservice.entity.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * users.role is a MySQL ENUM('customer','owner','admin') — lowercase.
 * The Java enum stays UPPERCASE (CUSTOMER/OWNER/ADMIN) for readability
 * everywhere else in the codebase (JWT claims, comparisons, etc.);
 * this converter is the one place the case translation happens.
 */
@Converter(autoApply = false)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role == null ? null : role.name().toLowerCase();
    }

    @Override
    public Role convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : Role.valueOf(dbValue.toUpperCase());
    }
}
