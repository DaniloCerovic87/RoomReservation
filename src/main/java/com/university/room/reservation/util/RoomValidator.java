package com.university.room.reservation.util;

import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.model.enums.RoomType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoomValidator {

    public static void validateRoom(String roomType, Integer numberOfComputers) {
        if (roomType.equals(RoomType.COMPUTER_ROOM.getValue()) &&
                (numberOfComputers == null || numberOfComputers <= 0)) {
            throw new ValidationException(MessageProperties.COMPUTER_INVALID_COUNT);
        }

        if ((roomType.equals(RoomType.CLASSROOM.getValue()) ||
                roomType.equalsIgnoreCase(RoomType.AMPHITHEATER.getValue())) && numberOfComputers != null) {
            throw new ValidationException(MessageProperties.COMPUTERS_NOT_ALLOWED);
        }
    }

}
