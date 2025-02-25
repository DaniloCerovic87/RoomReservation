package com.university.room.reservation.model.enums;

import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ValidationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    USER("User"),
    ADMIN("Admin");

    private final String value;

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new ValidationException(MessageProperties.UNKNOWN_ROLE, value);
    }

}
