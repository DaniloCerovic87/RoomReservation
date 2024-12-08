package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.dto.ReservationDto;
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
import com.university.RoomReservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        User user = userRepository.findById(reservationDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + reservationDto.getUserId()));
        Room room = roomRepository.findById(reservationDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + reservationDto.getUserId()));

        Reservation reservation = createSpecificReservation(reservationDto);

        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setStartTime(reservationDto.getStartTime());
        reservation.setEndTime(reservationDto.getEndTime());
        reservation.setReservationStatus(ReservationStatus.PENDING);

        reservation = reservationRepository.save(reservation);

        return ReservationMapper.mapToDto(reservation);
    }

    private Reservation createSpecificReservation(ReservationDto dto) {
        ReservationPurpose reservationPurpose = ReservationPurpose.fromValue(dto.getReservationPurpose());
        Reservation reservation = null;
        switch (reservationPurpose) {
            case CLASS -> {
                if (StringUtils.isBlank(dto.getSubject())) {
                    throw new IllegalArgumentException("Subject is required for class reservation.");
                }
                if (dto.getSemester() == null) {
                    throw new IllegalArgumentException("Semester is required for class reservation.");
                }

                if (StringUtils.isBlank(dto.getClassType())) {
                    throw new IllegalArgumentException("Class type is required for class reservation.");
                }

                ClassReservation classReservation = new ClassReservation();
                classReservation.setSubject(dto.getSubject());
                classReservation.setSemester(dto.getSemester());
                classReservation.setClassType(ClassType.fromValue(dto.getClassType()));
                reservation = classReservation;
            }
            case EXAM -> {
                if (StringUtils.isBlank(dto.getSubject())) {
                    throw new IllegalArgumentException("Subject is required for exam reservation.");
                }
                if (dto.getSemester() == null) {
                    throw new IllegalArgumentException("Semester is required for exam reservation.");
                }
                if (StringUtils.isBlank(dto.getExamType())) {
                    throw new IllegalArgumentException("Exam type is required for exam reservation.");
                }

                ExamReservation examReservation = new ExamReservation();
                examReservation.setSubject(dto.getSubject());
                examReservation.setSemester(dto.getSemester());
                examReservation.setExamType(ExamType.fromValue(dto.getExamType()));
                reservation = examReservation;
            }
            case MEETING -> {
                if (StringUtils.isBlank(dto.getMeetingName())) {
                    throw new IllegalArgumentException("Meeting name is required for meeting reservation.");
                }
                if (StringUtils.isBlank(dto.getMeetingDescription())) {
                    throw new IllegalArgumentException("Meeting description is required for meeting reservation.");
                }

                MeetingReservation meetingReservation = new MeetingReservation();
                meetingReservation.setMeetingName(dto.getMeetingName());
                meetingReservation.setMeetingDescription(dto.getMeetingDescription());
                reservation = meetingReservation;
            }
            case EVENT -> {
                if (StringUtils.isBlank(dto.getEventName())) {
                    throw new IllegalArgumentException("Event name is required for event reservation.");
                }
                if (StringUtils.isBlank(dto.getEventDescription())) {
                    throw new IllegalArgumentException("Event description is required for event reservation.");
                }

                EventReservation eventReservation = new EventReservation();
                eventReservation.setEventName(dto.getEventName());
                eventReservation.setEventDescription(dto.getEventDescription());
                reservation = eventReservation;
            }
        }
        return reservation;
    }

}
