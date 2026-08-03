package com.carrental.crudservice.entity.converter;

import com.carrental.crudservice.entity.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * users.status is a MySQL ENUM('active','blocked').
 */
@Converter(autoApply = false)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus status) {
        return status == null ? null : status.name().toLowerCase();
    }

    @Override
    public UserStatus convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : UserStatus.valueOf(dbValue.toUpperCase());
    }
}
