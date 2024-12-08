package com.university.RoomReservation.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomType {
    CLASSROOM("Classroom"),
    COMPUTER_ROOM("Computer Room"),
    AMPHITHEATER("Amphitheater");

    private final String value;

    public static RoomType fromValue(String value) {
        for (RoomType roomType : RoomType.values()) {
            if (roomType.value.equals(value)) {
                return roomType;
            }
        }
        throw new IllegalArgumentException("Unknown room type: " + value);
    }
}