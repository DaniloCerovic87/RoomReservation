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
import com.university.RoomReservation.request.ReservationFilterFormRequest;
import com.university.RoomReservation.request.ReservationRequest;
import com.university.RoomReservation.service.ReservationService;
import com.university.RoomReservation.util.ReservationValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.university.RoomReservation.constants.MessageProperties.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    private final EntityManager entityManager;

    @Override
    public Page<ReservationDTO> findReservations(ReservationFilterFormRequest filterForm, Pageable pageable) {
        Class<?> type = null;
        if (filterForm.getReservationPurpose() != null) {
            ReservationPurpose reservationPurpose = ReservationPurpose.fromValue(filterForm.getReservationPurpose());

            switch (reservationPurpose) {
                case CLASS -> type = ClassReservation.class;
                case EXAM -> type = ExamReservation.class;
                case MEETING -> type = MeetingReservation.class;
                case EVENT -> type = EventReservation.class;
            }
        }

        ReservationStatus reservationStatus = null;
        if (filterForm.getStatus() != null) {
            reservationStatus = ReservationStatus.fromValue(filterForm.getStatus());
        }

        return reservationRepository.findReservationsWithFilters(
                type,
                filterForm.getStartTime(),
                filterForm.getEndTime(),
                reservationStatus,
                filterForm.getRoomName(),
                pageable).map(ReservationMapper::toDTO);
    }

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


    @Override
    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationRequest request) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));

        ReservationPurpose reservationPurpose = ReservationPurpose.fromValue(request.getReservationPurpose());

        if (!existingReservation.getReservationPurpose().equals(reservationPurpose.name())) {
            clearPreviousReservationPurposeFields(existingReservation);
            updateReservationPurpose(id, reservationPurpose.name());
        }

        existingReservation = reservationRepository.getReferenceById(id);

        validateAndSetReservationPurposeFields(existingReservation, request);

        existingReservation.setUser(user);
        existingReservation.setRoom(room);
        existingReservation.setStartTime(request.getStartTime());
        existingReservation.setEndTime(request.getEndTime());
        existingReservation.setReservationStatus(ReservationStatus.PENDING);

        return ReservationMapper.toDTO(reservationRepository.save(existingReservation));
    }

    private void updateReservationPurpose(Long id, String newPurpose) {
        String sqlUpdate = "UPDATE reservation SET reservation_purpose = ? WHERE id = ?";
        Query query = entityManager.createNativeQuery(sqlUpdate);
        query.setParameter(1, newPurpose);
        query.setParameter(2, id);
        query.executeUpdate();

        entityManager.clear();
        Reservation updatedReservation = entityManager.find(Reservation.class, id);
        entityManager.refresh(updatedReservation);
    }

    private void clearPreviousReservationPurposeFields(Reservation reservation) {
        switch (ReservationPurpose.fromValue(reservation.getReservationPurpose())) {
            case CLASS -> {
                ClassReservation classReservation = (ClassReservation) reservation;
                classReservation.setSubject(null);
                classReservation.setSemester(null);
                classReservation.setClassType(null);
            }
            case EXAM -> {
                ExamReservation examReservation = (ExamReservation) reservation;
                examReservation.setSubject(null);
                examReservation.setSemester(null);
                examReservation.setExamType(null);
            }
            case MEETING -> {
                MeetingReservation meetingReservation = (MeetingReservation) reservation;
                meetingReservation.setMeetingName(null);
                meetingReservation.setMeetingDescription(null);
            }
            case EVENT -> {
                EventReservation eventReservation = (EventReservation) reservation;
                eventReservation.setEventName(null);
                eventReservation.setEventDescription(null);
            }
        }
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

    private void validateAndSetReservationPurposeFields(Reservation reservation, ReservationRequest request) {
        ReservationPurpose reservationPurpose = ReservationPurpose.fromValue(request.getReservationPurpose());
        switch (reservationPurpose) {
            case CLASS -> {
                ReservationValidator.validateClassReservation(request);

                ClassReservation classReservation = (ClassReservation) reservation;
                classReservation.setSubject(request.getSubject());
                classReservation.setSemester(request.getSemester());
                classReservation.setClassType(ClassType.fromValue(request.getClassType()));
            }
            case EXAM -> {
                ReservationValidator.validateExamReservation(request);

                ExamReservation examReservation = (ExamReservation) reservation;
                examReservation.setSubject(request.getSubject());
                examReservation.setSemester(request.getSemester());
                examReservation.setExamType(ExamType.fromValue(request.getExamType()));
            }
            case MEETING -> {
                ReservationValidator.validateMeetingReservation(request);

                MeetingReservation meetingReservation = (MeetingReservation) reservation;
                meetingReservation.setMeetingName(request.getMeetingName());
                meetingReservation.setMeetingDescription(request.getMeetingDescription());
            }
            case EVENT -> {
                ReservationValidator.validateEventReservation(request);

                EventReservation eventReservation = (EventReservation) reservation;
                eventReservation.setEventName(request.getEventName());
                eventReservation.setEventDescription(request.getEventDescription());
            }
        }
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
