package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.model.MeetingReservation;
import com.university.room.reservation.model.Reservation;
import com.university.room.reservation.model.Room;
import com.university.room.reservation.model.enums.ReservationStatus;
import com.university.room.reservation.model.enums.RoomType;
import com.university.room.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HistoricalRoomPreferenceServiceImplTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final HistoricalRoomPreferenceServiceImpl service =
            new HistoricalRoomPreferenceServiceImpl(reservationRepository);

    @Test
    void returnsRoomTypeWhenItHasAtLeastTwoReservationsAndMajorityOfRecentFive() {
        when(reservationRepository.findTop5ByUserIdAndReservationStatusInOrderByStartTimeDesc(1L, List.of(
                ReservationStatus.APPROVED
        ))).thenReturn(List.of(
                reservation(RoomType.COMPUTER_ROOM, 5),
                reservation(RoomType.COMPUTER_ROOM, 4),
                reservation(RoomType.COMPUTER_ROOM, 3),
                reservation(RoomType.CLASSROOM, 2),
                reservation(RoomType.CLASSROOM, 1)
        ));

        assertThat(service.findPreferredRoomType(1L))
                .contains(RoomType.COMPUTER_ROOM);
    }

    @Test
    void returnsEmptyWhenThereIsNoMajority() {
        when(reservationRepository.findTop5ByUserIdAndReservationStatusInOrderByStartTimeDesc(1L, List.of(
                ReservationStatus.APPROVED
        ))).thenReturn(List.of(
                reservation(RoomType.COMPUTER_ROOM, 4),
                reservation(RoomType.COMPUTER_ROOM, 3),
                reservation(RoomType.CLASSROOM, 2),
                reservation(RoomType.CLASSROOM, 1)
        ));

        assertThat(service.findPreferredRoomType(1L)).isEmpty();
    }

    @Test
    void returnsEmptyWhenThereAreFewerThanTwoReservations() {
        when(reservationRepository.findTop5ByUserIdAndReservationStatusInOrderByStartTimeDesc(1L, List.of(
                ReservationStatus.APPROVED
        ))).thenReturn(List.of(reservation(RoomType.COMPUTER_ROOM, 1)));

        assertThat(service.findPreferredRoomType(1L)).isEmpty();
    }

    private Reservation reservation(RoomType roomType, int daysAgo) {
        MeetingReservation reservation = new MeetingReservation();
        reservation.setRoom(Room.builder().roomType(roomType).build());
        reservation.setStartTime(LocalDateTime.now().minusDays(daysAgo));
        return reservation;
    }
}
