package com.university.RoomReservation.util;

import com.university.RoomReservation.exception.ValidationException;
import com.university.RoomReservation.model.enums.RoomType;
import lombok.experimental.UtilityClass;

import static com.university.RoomReservation.constants.MessageProperties.COMPUTERS_NOT_ALLOWED;
import static com.university.RoomReservation.constants.MessageProperties.COMPUTER_INVALID_COUNT;

@UtilityClass
public class RoomValidator {

    public static void validateRoom(String roomType, Integer numberOfComputers) {
        if (roomType.equals(RoomType.COMPUTER_ROOM.getValue()) &&
                (numberOfComputers == null || numberOfComputers <= 0)) {
            throw new ValidationException(COMPUTER_INVALID_COUNT);
        }

        if ((roomType.equals(RoomType.CLASSROOM.getValue()) ||
                roomType.equalsIgnoreCase(RoomType.AMPHITHEATER.getValue())) && numberOfComputers != null) {
            throw new ValidationException(COMPUTERS_NOT_ALLOWED);
        }
    }

}
