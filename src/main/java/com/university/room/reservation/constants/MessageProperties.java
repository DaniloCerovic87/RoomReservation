package com.university.room.reservation.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageProperties {

    public static final String USER_NOT_FOUND = "user.not.found";
    public static final String EMPLOYEE_NOT_FOUND = "employee.not.found";
    public static final String ROOM_NOT_FOUND = "room.not.found";
    public static final String RESERVATION_NOT_FOUND = "reservation.not.found";
    public static final String RESERVATION_EXISTS_FOR_ROOM = "reservation.exists.for.room";

    public static final String SUBJECT_REQUIRED_FOR_CLASS = "subject.required.for.class";
    public static final String SEMESTER_REQUIRED_FOR_CLASS = "semester.required.for.class";
    public static final String CLASS_TYPE_REQUIRED = "class.type.required";
    public static final String UNKNOWN_CLASS_TYPE = "unknown.class.type";


    public static final String SUBJECT_REQUIRED_FOR_EXAM = "subject.required.for.exam";
    public static final String SEMESTER_REQUIRED_FOR_EXAM = "semester.required.for.exam";
    public static final String EXAM_TYPE_REQUIRED = "exam.type.required";
    public static final String UNKNOWN_EXAM_TYPE = "unknown.exam.type";
    public static final String UNKNOWN_ROLE = "unknown.role";


    public static final String MEETING_NAME_REQUIRED = "meeting.name.required";
    public static final String MEETING_DESCRIPTION_REQUIRED = "meeting.description.required";
    public static final String EVENT_NAME_REQUIRED = "event.name.required";
    public static final String EVENT_DESCRIPTION_REQUIRED = "event.description.required";

    public static final String RESERVATION_USER_ID_NOT_NULL = "reservation.userId.notNull";
    public static final String RESERVATION_ROOM_ID_NOT_NULL = "reservation.roomId.notNull";
    public static final String RESERVATION_START_TIME_NOT_NULL = "reservation.startTime.notNull";
    public static final String RESERVATION_START_TIME_FUTURE_OR_PRESENT = "reservation.startTime.futureOrPresent";
    public static final String RESERVATION_END_TIME_NOT_NULL = "reservation.endTime.notNull";
    public static final String RESERVATION_END_TIME_FUTURE_OR_PRESENT = "reservation.endTime.futureOrPresent";
    public static final String RESERVATION_DURATION_TIME_INVALID = "reservation.duration.time.invalid";
    public static final String RESERVATION_PURPOSE_NOT_BLANK = "reservation.purpose.notBlank";
    public static final String UNKNOWN_RESERVATION_PURPOSE = "unknown.reservation.purpose";
    public static final String UNKNOWN_RESERVATION_STATUS = "unknown.reservation.status";


    public static final String UNKNOWN_ROOM_TYPE = "unknown.room.type";
    public static final String COMPUTER_INVALID_COUNT = "room.computer.invalid.count";
    public static final String COMPUTERS_NOT_ALLOWED = "room.computers.not.allowed";


    public static final String ROOM_NAME_NOT_BLANK = "room.name.not.blank";
    public static final String ROOM_TYPE_NOT_BLANK = "room.type.not.blank";
    public static final String ROOM_CAPACITY_NOT_NULL = "room.capacity.not.null";
    public static final String ROOM_CAPACITY_POSITIVE = "room.capacity.positive";

    public static final String EMPLOYEE_ID_NOT_NULL = "employee.id.not.null";
    public static final String ROLE_NOT_BLANK = "role.not.blank";
    public static final String USERNAME_NOT_BLANK = "username.not.blank";
    public static final String PASSWORD_NOT_BLANK = "password.not.blank";

    public static final String USER_ALREADY_ASSIGNED = "user.already.assigned";

}
