package com.university.RoomReservation.util;

import com.university.RoomReservation.exception.ValidationException;
import com.university.RoomReservation.request.CreateReservationRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import static com.university.RoomReservation.constants.MessageProperties.*;

@UtilityClass
public class ReservationValidator {

    public static void validateClassReservation(CreateReservationRequest request) {
        if (StringUtils.isBlank(request.getSubject())) {
            throw new ValidationException(SUBJECT_REQUIRED_FOR_CLASS);
        }
        if (request.getSemester() == null) {
            throw new ValidationException(SEMESTER_REQUIRED_FOR_CLASS);
        }
        if (StringUtils.isBlank(request.getClassType())) {
            throw new ValidationException(CLASS_TYPE_REQUIRED);
        }
    }

    public static void validateExamReservation(CreateReservationRequest request) {
        if (StringUtils.isBlank(request.getSubject())) {
            throw new ValidationException(SUBJECT_REQUIRED_FOR_EXAM);
        }
        if (request.getSemester() == null) {
            throw new ValidationException(SEMESTER_REQUIRED_FOR_EXAM);
        }
        if (StringUtils.isBlank(request.getExamType())) {
            throw new ValidationException(EXAM_TYPE_REQUIRED);
        }
    }

    public static void validateMeetingReservation(CreateReservationRequest request) {
        if (StringUtils.isBlank(request.getMeetingName())) {
            throw new ValidationException(MEETING_NAME_REQUIRED);
        }
        if (StringUtils.isBlank(request.getMeetingDescription())) {
            throw new ValidationException(MEETING_DESCRIPTION_REQUIRED);
        }
    }

    public static void validateEventReservation(CreateReservationRequest request) {
        if (StringUtils.isBlank(request.getEventName())) {
            throw new ValidationException(EVENT_NAME_REQUIRED);
        }
        if (StringUtils.isBlank(request.getEventDescription())) {
            throw new ValidationException(EVENT_DESCRIPTION_REQUIRED);
        }
    }

}
