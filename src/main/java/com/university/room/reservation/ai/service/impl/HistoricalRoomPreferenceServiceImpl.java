package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.service.HistoricalRoomPreferenceService;
import com.university.room.reservation.model.Reservation;
import com.university.room.reservation.model.Room;
import com.university.room.reservation.model.enums.ReservationStatus;
import com.university.room.reservation.model.enums.RoomType;
import com.university.room.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoricalRoomPreferenceServiceImpl implements HistoricalRoomPreferenceService {

    private static final int MINIMUM_RESERVATIONS_FOR_PREFERENCE = 2;
    private static final Collection<ReservationStatus> RELEVANT_STATUSES = List.of(
            ReservationStatus.APPROVED
    );

    private final ReservationRepository reservationRepository;

    @Override
    public Optional<RoomType> findPreferredRoomType(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }

        List<Reservation> recentReservations = reservationRepository
                .findTop5ByUserIdAndReservationStatusInOrderByStartTimeDesc(userId, RELEVANT_STATUSES);

        if (recentReservations.size() < MINIMUM_RESERVATIONS_FOR_PREFERENCE) {
            return Optional.empty();
        }

        Map<RoomType, Long> reservationsByRoomType = recentReservations.stream()
                .map(Reservation::getRoom)
                .filter(room -> room != null && room.getRoomType() != null)
                .map(Room::getRoomType)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return reservationsByRoomType.entrySet().stream()
                .filter(entry -> entry.getValue() >= MINIMUM_RESERVATIONS_FOR_PREFERENCE)
                .filter(entry -> entry.getValue() * 2 > recentReservations.size())
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
}
