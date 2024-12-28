package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.dto.RoomDTO;
import com.university.RoomReservation.dto.UserDTO;
import com.university.RoomReservation.exception.ResourceNotFoundException;
import com.university.RoomReservation.mapper.ReservationMapper;
import com.university.RoomReservation.mapper.RoomMapper;
import com.university.RoomReservation.mapper.UserMapper;
import com.university.RoomReservation.model.*;
import com.university.RoomReservation.model.enums.ClassType;
import com.university.RoomReservation.model.enums.ExamType;
import com.university.RoomReservation.model.enums.ReservationPurpose;
import com.university.RoomReservation.model.enums.ReservationStatus;
import com.university.RoomReservation.repository.ReservationRepository;
import com.university.RoomReservation.request.CreateReservationRequest;
import com.university.RoomReservation.service.ReservationService;
import com.university.RoomReservation.service.RoomService;
import com.university.RoomReservation.service.UserService;
import com.university.RoomReservation.util.ReservationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.university.RoomReservation.constants.MessageProperties.RESERVATION_NOT_FOUND;
import static com.university.RoomReservation.constants.MessageProperties.ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserService userService;

    private final RoomService roomService;

    private final ReservationRepository reservationRepository;

    public List<ReservationDTO> getReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId).stream()
                .map(ReservationMapper::toDTO)
                .toList();
    }

    @Override
    public ReservationDTO createReservation(CreateReservationRequest request) {
        UserDTO userDTO = userService.getUserById(request.getUserId());
        RoomDTO roomDTO = roomService.getRoomById(request.getRoomId());

        Reservation reservation = createSpecificReservation(request);

        reservation.setUser(UserMapper.toEntity(userDTO));
        reservation.setRoom(RoomMapper.toRoom(roomDTO));
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setReservationStatus(ReservationStatus.PENDING);

        reservation = reservationRepository.save(reservation);

        return ReservationMapper.toDTO(reservation);
    }

    private Reservation createSpecificReservation(CreateReservationRequest request) {
        ReservationPurpose reservationPurpose = ReservationPurpose.fromValue(request.getReservationPurpose());
        Reservation reservation = null;
        switch (reservationPurpose) {
            case CLASS -> {
                ReservationValidator.validateClassReservation(request);

                ClassReservation classReservation = new ClassReservation();
                classReservation.setSubject(request.getSubject());
                classReservation.setSemester(request.getSemester());
                classReservation.setClassType(ClassType.fromValue(request.getClassType()));
                reservation = classReservation;
            }
            case EXAM -> {
                ReservationValidator.validateExamReservation(request);

                ExamReservation examReservation = new ExamReservation();
                examReservation.setSubject(request.getSubject());
                examReservation.setSemester(request.getSemester());
                examReservation.setExamType(ExamType.fromValue(request.getExamType()));
                reservation = examReservation;
            }
            case MEETING -> {
                ReservationValidator.validateMeetingReservation(request);

                MeetingReservation meetingReservation = new MeetingReservation();
                meetingReservation.setMeetingName(request.getMeetingName());
                meetingReservation.setMeetingDescription(request.getMeetingDescription());
                reservation = meetingReservation;
            }
            case EVENT -> {
                ReservationValidator.validateEventReservation(request);

                EventReservation eventReservation = new EventReservation();
                eventReservation.setEventName(request.getEventName());
                eventReservation.setEventDescription(request.getEventDescription());
                reservation = eventReservation;
            }
        }
        return reservation;
    }

    @Override
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException(RESERVATION_NOT_FOUND);
        }
        reservationRepository.deleteById(id);
    }
}
