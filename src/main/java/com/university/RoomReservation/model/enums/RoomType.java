package com.university.RoomReservation.model.enums;

import com.university.RoomReservation.constants.MessageProperties;
import com.university.RoomReservation.exception.ValidationException;
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
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new ValidationException(MessageProperties.UNKNOWN_ROOM_TYPE, value);
    }

}