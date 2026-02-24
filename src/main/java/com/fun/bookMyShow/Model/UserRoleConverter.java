package com.fun.bookMyShow.Model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<User.Role, String> {

    @Override
    public String convertToDatabaseColumn(User.Role role) {
        return role == null ? User.Role.USER.name() : role.name();
    }

    @Override
    public User.Role convertToEntityAttribute(String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            return User.Role.USER;
        }

        String normalized = dbValue.trim().toUpperCase();
        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring(5);
        }

        try {
            return User.Role.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return User.Role.USER;
        }
    }
}
