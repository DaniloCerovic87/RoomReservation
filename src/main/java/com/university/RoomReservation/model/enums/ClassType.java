package com.university.RoomReservation.model.enums;

import com.university.RoomReservation.constants.MessageProperties;
import com.university.RoomReservation.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClassType {
    LECTURE("Lecture"),
    EXERCISE("Exercise");

    private final String value;

    public static ClassType fromValue(String value) {
        for (ClassType purpose : ClassType.values()) {
            if (purpose.value.equalsIgnoreCase(value)) {
                return purpose;
            }
        }
        throw new ValidationException(MessageProperties.UNKNOWN_CLASS_TYPE, value);
    }
}