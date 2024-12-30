package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.dto.ReservationDTO;
import com.university.RoomReservation.exception.ResourceNotFoundException;
import com.university.RoomReservation.mapper.ReservationMapper;
import com.university.RoomReservation.model.*;
import com.university.RoomReservation.model.enums.ClassType;
import com.university.RoomReservation.model.enums.ExamType;
import com.university.RoomReservation.model.enums.ReservationPurpose;
import com.university.RoomReservation.model.enums.ReservationStatus;
import com.university.RoomReservation.repository.ReservationRepository;
import com.university.RoomReservation.repository.RoomRepository;
import com.university.RoomReservation.repository.UserRepository;
import com.university.RoomReservation.request.ReservationRequest;
import com.university.RoomReservation.service.ReservationService;
import com.university.RoomReservation.util.ReservationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.university.RoomReservation.constants.MessageProperties.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;


    public List<ReservationDTO> getReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId).stream()
                .map(ReservationMapper::toDTO)
                .toList();
    }

    @Override
    public ReservationDTO createReservation(ReservationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));

        Reservation reservation = createSpecificReservation(request);

        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setReservationStatus(ReservationStatus.PENDING);

        reservation = reservationRepository.save(reservation);

        return ReservationMapper.toDTO(reservation);
    }

    private Reservation createSpecificReservation(ReservationRequest request) {
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

    @Override
    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_NOT_FOUND));
        return ReservationMapper.toDTO(reservation);
    }
}
