package com.university.room.reservation.util;

import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.request.ReservationRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;

@UtilityClass
public class ReservationValidator {

    private static final int RESERVATION_SLOT_MINUTES = 15;

    public static void validateClassReservation(ReservationRequest request) {
        if (StringUtils.isBlank(request.getSubject())) {
            throw new ValidationException(MessageProperties.SUBJECT_REQUIRED_FOR_CLASS);
        }
        if (request.getSemester() == null) {
            throw new ValidationException(MessageProperties.SEMESTER_REQUIRED_FOR_CLASS);
        }
        if (StringUtils.isBlank(request.getClassType())) {
            throw new ValidationException(MessageProperties.CLASS_TYPE_REQUIRED);
        }
    }

    public static void validateExamReservation(ReservationRequest request) {
        if (StringUtils.isBlank(request.getSubject())) {
            throw new ValidationException(MessageProperties.SUBJECT_REQUIRED_FOR_EXAM);
        }
        if (request.getSemester() == null) {
            throw new ValidationException(MessageProperties.SEMESTER_REQUIRED_FOR_EXAM);
        }
        if (StringUtils.isBlank(request.getExamType())) {
            throw new ValidationException(MessageProperties.EXAM_TYPE_REQUIRED);
        }
    }

    public static void validateMeetingReservation(ReservationRequest request) {
        if (StringUtils.isBlank(request.getMeetingName())) {
            throw new ValidationException(MessageProperties.MEETING_NAME_REQUIRED);
        }
        if (StringUtils.isBlank(request.getMeetingDescription())) {
            throw new ValidationException(MessageProperties.MEETING_DESCRIPTION_REQUIRED);
        }
    }

    public static void validateEventReservation(ReservationRequest request) {
        if (StringUtils.isBlank(request.getEventName())) {
            throw new ValidationException(MessageProperties.EVENT_NAME_REQUIRED);
        }
        if (StringUtils.isBlank(request.getEventDescription())) {
            throw new ValidationException(MessageProperties.EVENT_DESCRIPTION_REQUIRED);
        }
    }

    public static void validateReservationDuration(ReservationRequest request) {
        long durationInMinutes = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        if (durationInMinutes < RESERVATION_SLOT_MINUTES) {
            throw new ValidationException(MessageProperties.RESERVATION_DURATION_TIME_INVALID);
        }

        if (!isOnReservationSlot(request.getStartTime()) || !isOnReservationSlot(request.getEndTime())) {
            throw new ValidationException(MessageProperties.RESERVATION_TIME_SLOT_INVALID, RESERVATION_SLOT_MINUTES);
        }
    }

    private static boolean isOnReservationSlot(LocalDateTime dateTime) {
        return dateTime.getMinute() % RESERVATION_SLOT_MINUTES == 0
                && dateTime.getSecond() == 0
                && dateTime.getNano() == 0;
    }

}
