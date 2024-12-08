package com.university.RoomReservation.mapper;

import com.university.RoomReservation.dto.ReservationDto;
import com.university.RoomReservation.model.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservationMapper {

    public static ReservationDto mapToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setUserId(reservation.getUser().getId());
        dto.setRoomId(reservation.getRoom().getId());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setReservationPurpose(reservation.getReservationPurpose());

        if (reservation instanceof ClassReservation classReservation) {
            dto.setSubject(classReservation.getSubject());
            dto.setSemester(classReservation.getSemester());
            dto.setClassType(classReservation.getClassType().getValue());
        } else if (reservation instanceof ExamReservation examReservation) {
            dto.setSubject(examReservation.getSubject());
            dto.setSemester(examReservation.getSemester());
            dto.setExamType(examReservation.getExamType().getValue());
        } else if (reservation instanceof MeetingReservation meetingReservation) {
            dto.setMeetingTitle(meetingReservation.getMeetingName());
            dto.setMeetingDescription(meetingReservation.getMeetingDescription());
        } else if (reservation instanceof EventReservation eventReservation) {
            dto.setEventName(eventReservation.getEventName());
            dto.setEventDescription(eventReservation.getEventDescription());
        }

        return dto;
    }

}
