package com.university.room.reservation.ai.tool;

import com.university.room.reservation.ai.agent.RecommendationAgent;
import com.university.room.reservation.ai.context.AiConversationContext;
import com.university.room.reservation.ai.dto.RecommendationCriteria;
import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.enums.CapacityPreference;
import com.university.room.reservation.ai.service.AiToolCallLogService;
import com.university.room.reservation.ai.service.HistoricalRoomPreferenceService;
import com.university.room.reservation.ai.service.RoomRecommendationService;
import com.university.room.reservation.ai.store.PendingMeetingReservationStore;
import com.university.room.reservation.ai.request.RoomSearchRequest;
import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.service.ReservationService;
import com.university.room.reservation.service.RoomService;
import com.university.room.reservation.model.enums.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationToolsHistoricalPreferenceTest {

    @Mock
    private RoomService roomService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private PendingMeetingReservationStore pendingMeetingReservationStore;

    @Mock
    private ReservationService reservationService;

    @Mock
    private RoomRecommendationService roomRecommendationService;

    @Mock
    private RecommendationAgent recommendationAgent;

    @Mock
    private HistoricalRoomPreferenceService historicalRoomPreferenceService;

    @Mock
    private AiToolCallLogService aiToolCallLogService;

    @Mock
    private AiConversationContext aiConversationContext;

    private ReservationTools reservationTools;

    @BeforeEach
    void setUp() {
        reservationTools = new ReservationTools(
                roomService,
                messageSource,
                pendingMeetingReservationStore,
                reservationService,
                roomRecommendationService,
                historicalRoomPreferenceService,
                recommendationAgent,
                aiToolCallLogService,
                aiConversationContext
        );

        when(aiConversationContext.getUserId()).thenReturn(Optional.of(1L));
        when(roomService.findAvailableRooms(any(), any(), any(), any()))
                .thenReturn(List.of(room(1L, RoomType.CLASSROOM, 40)));
        when(roomRecommendationService.recommendRoom(any(), any(), any()))
                .thenReturn(RoomRecommendationResponse.builder().build());
    }

    @Test
    void appliesHistoricalRoomTypeWhenCurrentRequestHasNoExplicitRoomType() {
        when(historicalRoomPreferenceService.findPreferredRoomType(1L))
                .thenReturn(Optional.of(RoomType.COMPUTER_ROOM));

        reservationTools.findAvailableRooms(request(null, null));

        RoomSearchRequest loggedRequest = capturedToolInput();

        assertThat(loggedRequest.getPreferredRoomType()).isEqualTo(RoomType.COMPUTER_ROOM);
    }

    @Test
    void leavesRoomTypeEmptyWhenHistoryHasNoClearMajority() {
        when(historicalRoomPreferenceService.findPreferredRoomType(1L))
                .thenReturn(Optional.empty());

        reservationTools.findAvailableRooms(request(null, null));

        RoomSearchRequest loggedRequest = capturedToolInput();

        assertThat(loggedRequest.getPreferredRoomType()).isNull();
    }

    @Test
    void currentExplicitRoomTypeOverridesHistoricalPreference() {
        when(recommendationAgent.interpret("Ovog puta želim običnu učionicu."))
                .thenReturn(RecommendationCriteria.builder()
                        .preferredRoomType(RoomType.CLASSROOM)
                        .capacityPreference(CapacityPreference.CLOSEST_MATCH)
                        .build());

        reservationTools.findAvailableRooms(request(
                "Ovog puta želim običnu učionicu.",
                RoomType.CLASSROOM
        ));

        RoomSearchRequest loggedRequest = capturedToolInput();

        assertThat(loggedRequest.getPreferredRoomType()).isEqualTo(RoomType.CLASSROOM);
    }

    private RoomSearchRequest request(String recommendationQuery, RoomType preferredRoomType) {
        RoomSearchRequest request = new RoomSearchRequest();
        request.setConversationId("history-test");
        request.setDate(LocalDate.of(2026, 11, 1));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setCapacity(8);
        request.setRecommendationQuery(recommendationQuery);
        request.setPreferredRoomType(preferredRoomType);
        return request;
    }

    private RoomSearchRequest capturedToolInput() {
        ArgumentCaptor<RoomSearchRequest> captor = ArgumentCaptor.forClass(RoomSearchRequest.class);
        verify(aiToolCallLogService).logToolCall(
                eq("history-test"),
                eq(1L),
                eq("findAvailableRooms"),
                captor.capture(),
                any(),
                eq(true),
                isNull()
        );
        return captor.getValue();
    }

    private RoomDTO room(Long id, RoomType roomType, int capacity) {
        return RoomDTO.builder()
                .id(id)
                .name("Test Room")
                .roomType(roomType.getValue())
                .capacity(capacity)
                .build();
    }
}
