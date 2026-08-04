package com.carrental.crudservice.entity.converter;

import com.carrental.crudservice.entity.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * users.role is a MySQL ENUM('customer','owner','admin') — lowercase.
 * The Java enum stays UPPERCASE for consistency with user-service; this
 * converter is the one place the case translation happens.
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
