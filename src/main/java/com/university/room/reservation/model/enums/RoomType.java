package com.university.room.reservation.model.enums;

import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ValidationException;
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
        for (RoomType type : RoomType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new ValidationException(MessageProperties.UNKNOWN_ROOM_TYPE, value);
    }

}