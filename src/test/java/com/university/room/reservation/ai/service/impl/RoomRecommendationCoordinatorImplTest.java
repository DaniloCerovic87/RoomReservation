package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.agent.RecommendationAgent;
import com.university.room.reservation.ai.dto.RecommendationCriteria;
import com.university.room.reservation.ai.dto.RoomRecommendationDecision;
import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.enums.CapacityPreference;
import com.university.room.reservation.ai.service.HistoricalRoomPreferenceService;
import com.university.room.reservation.ai.service.RoomRecommendationService;
import com.university.room.reservation.ai.request.RoomSearchRequest;
import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.model.enums.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomRecommendationCoordinatorImplTest {

    @Mock
    private RecommendationAgent recommendationAgent;

    @Mock
    private HistoricalRoomPreferenceService historicalRoomPreferenceService;

    @Mock
    private RoomRecommendationService roomRecommendationService;

    private RoomRecommendationCoordinatorImpl coordinator;

    @BeforeEach
    void setUp() {
        coordinator = new RoomRecommendationCoordinatorImpl(
                recommendationAgent,
                historicalRoomPreferenceService,
                roomRecommendationService
        );

        when(roomRecommendationService.recommendRoom(any(), eq(8), any()))
                .thenReturn(RoomRecommendationResponse.builder().build());
    }

    @Test
    void usesHistoricalRoomTypeWhenCurrentRequestHasNoExplicitRoomType() {
        when(historicalRoomPreferenceService.findPreferredRoomType(1L))
                .thenReturn(Optional.of(RoomType.COMPUTER_ROOM));

        RoomSearchRequest request = request(null);
        RoomRecommendationDecision decision = coordinator.recommend(rooms(), request, 1L);

        assertThat(decision.criteria().getPreferredRoomType()).isEqualTo(RoomType.COMPUTER_ROOM);
    }

    @Test
    void leavesRoomTypeEmptyWhenHistoryHasNoClearMajority() {
        when(historicalRoomPreferenceService.findPreferredRoomType(1L))
                .thenReturn(Optional.empty());

        RoomRecommendationDecision decision = coordinator.recommend(rooms(), request(null), 1L);

        assertThat(decision.criteria().getPreferredRoomType()).isNull();
    }

    @Test
    void currentExplicitRoomTypeOverridesHistoricalPreference() {
        when(recommendationAgent.interpret("Ovog puta želim običnu učionicu."))
                .thenReturn(RecommendationCriteria.builder()
                        .preferredRoomType(RoomType.CLASSROOM)
                        .capacityPreference(CapacityPreference.CLOSEST_MATCH)
                        .build());
        RoomSearchRequest request = request("Ovog puta želim običnu učionicu.");
        RoomRecommendationDecision decision = coordinator.recommend(rooms(), request, 1L);

        assertThat(decision.criteria().getPreferredRoomType()).isEqualTo(RoomType.CLASSROOM);
    }

    @Test
    void usesDeterministicRequestValuesWhenRecommendationAgentFails() {
        when(recommendationAgent.interpret("komotnija sala"))
                .thenThrow(new RuntimeException("model unavailable"));

        RoomSearchRequest request = request("komotnija sala");

        RoomRecommendationDecision decision = coordinator.recommend(rooms(), request, 1L);

        assertThat(decision.criteria().getPreferredRoomType()).isNull();
        assertThat(decision.criteria().effectiveCapacityPreference())
                .isEqualTo(CapacityPreference.CLOSEST_MATCH);
        verify(roomRecommendationService).recommendRoom(any(), eq(8), any());
    }

    private RoomSearchRequest request(String recommendationQuery) {
        RoomSearchRequest request = new RoomSearchRequest();
        request.setDate(LocalDate.of(2026, 11, 1));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setCapacity(8);
        request.setRecommendationQuery(recommendationQuery);
        return request;
    }

    private List<RoomDTO> rooms() {
        return List.of(RoomDTO.builder().id(1L).name("Test Room").roomType("Classroom").capacity(40).build());
    }

}
