package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.exception.ResourceNotFoundException;
import com.university.RoomReservation.exception.ValidationException;
import com.university.RoomReservation.mapper.ReservationMapper;
import com.university.RoomReservation.model.*;
import com.university.RoomReservation.model.enums.ClassType;
import com.university.RoomReservation.model.enums.ExamType;
import com.university.RoomReservation.model.enums.ReservationPurpose;
import com.university.RoomReservation.model.enums.ReservationStatus;
import com.university.RoomReservation.repository.ReservationRepository;
import com.university.RoomReservation.repository.RoomRepository;
import com.university.RoomReservation.repository.UserRepository;
import com.university.RoomReservation.request.CreateReservationRequest;
import com.university.RoomReservation.response.ReservationResponse;
import com.university.RoomReservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.university.RoomReservation.constants.MessageProperties.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    @Override
    public ReservationResponse createReservation(CreateReservationRequest request) {
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

        return ReservationMapper.mapToDto(reservation);
    }

    private Reservation createSpecificReservation(CreateReservationRequest dto) {
        ReservationPurpose reservationPurpose = ReservationPurpose.fromValue(dto.getReservationPurpose());
        Reservation reservation = null;
        switch (reservationPurpose) {
            case CLASS -> {
                if (StringUtils.isBlank(dto.getSubject())) {
                    throw new ValidationException(SUBJECT_REQUIRED_FOR_CLASS);
                }
                if (dto.getSemester() == null) {
                    throw new ValidationException(SEMESTER_REQUIRED_FOR_CLASS);
                }

                if (StringUtils.isBlank(dto.getClassType())) {
                    throw new ValidationException(CLASS_TYPE_REQUIRED);
                }

                ClassReservation classReservation = new ClassReservation();
                classReservation.setSubject(dto.getSubject());
                classReservation.setSemester(dto.getSemester());
                classReservation.setClassType(ClassType.fromValue(dto.getClassType()));
                reservation = classReservation;
            }
            case EXAM -> {
                if (StringUtils.isBlank(dto.getSubject())) {
                    throw new ValidationException(SUBJECT_REQUIRED_FOR_EXAM);
                }
                if (dto.getSemester() == null) {
                    throw new ValidationException(SEMESTER_REQUIRED_FOR_EXAM);
                }
                if (StringUtils.isBlank(dto.getExamType())) {
                    throw new ValidationException(EXAM_TYPE_REQUIRED);
                }

                ExamReservation examReservation = new ExamReservation();
                examReservation.setSubject(dto.getSubject());
                examReservation.setSemester(dto.getSemester());
                examReservation.setExamType(ExamType.fromValue(dto.getExamType()));
                reservation = examReservation;
            }
            case MEETING -> {
                if (StringUtils.isBlank(dto.getMeetingName())) {
                    throw new ValidationException(MEETING_NAME_REQUIRED);
                }
                if (StringUtils.isBlank(dto.getMeetingDescription())) {
                    throw new ValidationException(MEETING_DESCRIPTION_REQUIRED);
                }

                MeetingReservation meetingReservation = new MeetingReservation();
                meetingReservation.setMeetingName(dto.getMeetingName());
                meetingReservation.setMeetingDescription(dto.getMeetingDescription());
                reservation = meetingReservation;
            }
            case EVENT -> {
                if (StringUtils.isBlank(dto.getEventName())) {
                    throw new ValidationException(EVENT_NAME_REQUIRED);
                }
                if (StringUtils.isBlank(dto.getEventDescription())) {
                    throw new ValidationException(EVENT_DESCRIPTION_REQUIRED);
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
