package com.university.RoomReservation.model.enums;

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
            if (purpose.value.equals(value)) {
                return purpose;
            }
        }
        throw new IllegalArgumentException("Unknown class type: " + value);
    }
}