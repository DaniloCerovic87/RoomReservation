package com.university.room.reservation.util;

import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.model.enums.RoomType;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@UtilityClass
public class RoomValidator {

    private static final int RESERVATION_SLOT_MINUTES = 15;

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

    public static void validateAvailabilitySearch(LocalDate date, LocalTime startTime, LocalTime endTime, Integer capacity) {
        if (date == null) {
            throw new ValidationException(MessageProperties.ROOM_AVAILABILITY_DATE_NOT_NULL);
        }
        if (startTime == null) {
            throw new ValidationException(MessageProperties.ROOM_AVAILABILITY_START_TIME_NOT_NULL);
        }
        if (endTime == null) {
            throw new ValidationException(MessageProperties.ROOM_AVAILABILITY_END_TIME_NOT_NULL);
        }
        if (capacity == null) {
            throw new ValidationException(MessageProperties.ROOM_AVAILABILITY_CAPACITY_NOT_NULL);
        }
        if (capacity <= 0) {
            throw new ValidationException(MessageProperties.ROOM_AVAILABILITY_CAPACITY_POSITIVE);
        }
        if (!endTime.isAfter(startTime)) {
            throw new ValidationException(MessageProperties.ROOM_AVAILABILITY_TIME_INVALID);
        }
        if (LocalDateTime.of(date, startTime).isBefore(LocalDateTime.now())) {
            throw new ValidationException(MessageProperties.ROOM_AVAILABILITY_START_TIME_FUTURE_OR_PRESENT);
        }
        if (!isOnReservationSlot(startTime) || !isOnReservationSlot(endTime)) {
            throw new ValidationException(MessageProperties.ROOM_AVAILABILITY_TIME_SLOT_INVALID, RESERVATION_SLOT_MINUTES);
        }
    }

    private static boolean isOnReservationSlot(LocalTime time) {
        return time.getMinute() % RESERVATION_SLOT_MINUTES == 0
                && time.getSecond() == 0
                && time.getNano() == 0;
    }

}
