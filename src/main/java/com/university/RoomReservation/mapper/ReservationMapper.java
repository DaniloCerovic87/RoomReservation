package com.university.RoomReservation.mapper;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.model.*;
import com.university.RoomReservation.model.enums.ReservationPurpose;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservationMapper {

    public static ReservationDTO toDTO(Reservation reservation) {
        ReservationDTO response = new ReservationDTO();
        response.setId(reservation.getId());
        response.setUserId(reservation.getUser().getId());
        response.setRoomId(reservation.getRoom().getId());
        response.setStartTime(reservation.getStartTime());
        response.setEndTime(reservation.getEndTime());
        response.setReservationPurpose(ReservationPurpose.fromValue(reservation.getReservationPurpose()).getValue());

        if (reservation instanceof ClassReservation classReservation) {
            response.setSubject(classReservation.getSubject());
            response.setSemester(classReservation.getSemester());
            response.setClassType(classReservation.getClassType().getValue());
        } else if (reservation instanceof ExamReservation examReservation) {
            response.setSubject(examReservation.getSubject());
            response.setSemester(examReservation.getSemester());
            response.setExamType(examReservation.getExamType().getValue());
        } else if (reservation instanceof MeetingReservation meetingReservation) {
            response.setMeetingName(meetingReservation.getMeetingName());
            response.setMeetingDescription(meetingReservation.getMeetingDescription());
        } else if (reservation instanceof EventReservation eventReservation) {
            response.setEventName(eventReservation.getEventName());
            response.setEventDescription(eventReservation.getEventDescription());
        }

        return response;
    }

}
