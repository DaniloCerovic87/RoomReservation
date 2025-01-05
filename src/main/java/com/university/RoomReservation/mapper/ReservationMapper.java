package com.university.RoomReservation.mapper;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.model.*;
import com.university.RoomReservation.model.enums.ReservationPurpose;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservationMapper {

    public static ReservationDTO toDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        User user = reservation.getUser();

        reservationDTO.setUserId(user.getId());
        Room room = reservation.getRoom();

        reservationDTO.setRoomId(room.getId());
        reservationDTO.setRoomName(room.getName());

        Employee employee = user.getEmployee();
        reservationDTO.setEmployeeFullName(employee.getFirstName() + " " + employee.getLastName());
        reservationDTO.setStartTime(reservation.getStartTime());
        reservationDTO.setEndTime(reservation.getEndTime());
        reservationDTO.setReservationPurpose(ReservationPurpose.fromValue(reservation.getReservationPurpose()).getValue());

        if (reservation instanceof ClassReservation classReservation) {
            reservationDTO.setSubject(classReservation.getSubject());
            reservationDTO.setSemester(classReservation.getSemester());
            reservationDTO.setClassType(classReservation.getClassType().getValue());
        } else if (reservation instanceof ExamReservation examReservation) {
            reservationDTO.setSubject(examReservation.getSubject());
            reservationDTO.setSemester(examReservation.getSemester());
            reservationDTO.setExamType(examReservation.getExamType().getValue());
        } else if (reservation instanceof MeetingReservation meetingReservation) {
            reservationDTO.setMeetingName(meetingReservation.getMeetingName());
            reservationDTO.setMeetingDescription(meetingReservation.getMeetingDescription());
        } else if (reservation instanceof EventReservation eventReservation) {
            reservationDTO.setEventName(eventReservation.getEventName());
            reservationDTO.setEventDescription(eventReservation.getEventDescription());
        }

        return reservationDTO;
    }

}
