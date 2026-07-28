package com.carrental.userservice.entity.converter;

import com.carrental.userservice.entity.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * users.status is a MySQL ENUM('active','pending_admin','blocked').
 * PENDING_ADMIN <-> "pending_admin".
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
