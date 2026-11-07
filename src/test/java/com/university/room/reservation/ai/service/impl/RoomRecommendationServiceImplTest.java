package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.dto.RecommendationCriteria;
import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.enums.CapacityPreference;
import com.university.room.reservation.ai.enums.ComputerRequirement;
import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.model.enums.RoomType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoomRecommendationServiceImplTest {

    private final RoomRecommendationServiceImpl service = new RoomRecommendationServiceImpl();

    @Test
    void recommendsClosestCapacityMatchByDefault() {
        RecommendationCriteria criteria = RecommendationCriteria.builder().build();

        RoomRecommendationResponse response = service.recommendRoom(
                List.of(
                        room(1L, "Room 1", RoomType.CLASSROOM, 20, null),
                        room(2L, "Room 2", RoomType.CLASSROOM, 12, null)
                ),
                10,
                criteria
        );

        assertThat(response.getRecommendedRoom().getName()).isEqualTo("Room 2");
    }

    @Test
    void filtersByPreferredRoomTypeBeforeRanking() {
        RecommendationCriteria criteria = RecommendationCriteria.builder()
                .capacityPreference(CapacityPreference.CLOSEST_MATCH)
                .preferredRoomType(RoomType.COMPUTER_ROOM)
                .computerRequirement(ComputerRequirement.AT_LEAST_ONE)
                .build();

        RoomRecommendationResponse response = service.recommendRoom(
                List.of(
                        room(1L, "Classroom", RoomType.CLASSROOM, 10, null),
                        room(2L, "Computer Room", RoomType.COMPUTER_ROOM, 20, 10)
                ),
                10,
                criteria
        );

        assertThat(response.getRecommendedRoom().getName()).isEqualTo("Computer Room");
        assertThat(response.getAlternatives()).isEmpty();
    }

    @Test
    void requiresOneComputerPerParticipantWhenExplicitlyRequested() {
        RecommendationCriteria criteria = RecommendationCriteria.builder()
                .preferredRoomType(RoomType.COMPUTER_ROOM)
                .computerRequirement(ComputerRequirement.ONE_PER_PARTICIPANT)
                .build();

        RoomRecommendationResponse response = service.recommendRoom(
                List.of(
                        room(1L, "Shared Computers", RoomType.COMPUTER_ROOM, 20, 5),
                        room(2L, "Computer For Everyone", RoomType.COMPUTER_ROOM, 20, 20)
                ),
                20,
                criteria
        );

        assertThat(response.getRecommendedRoom().getName()).isEqualTo("Computer For Everyone");
        assertThat(response.getReason()).contains("computer for every participant");
    }

    @Test
    void appliesMostSpaciousPreferenceAfterHardFilters() {
        RecommendationCriteria criteria = RecommendationCriteria.builder()
                .capacityPreference(CapacityPreference.MOST_SPACIOUS)
                .preferredRoomType(RoomType.COMPUTER_ROOM)
                .computerRequirement(ComputerRequirement.AT_LEAST_ONE)
                .build();

        RoomRecommendationResponse response = service.recommendRoom(
                List.of(
                        room(1L, "Small Computer Room", RoomType.COMPUTER_ROOM, 20, 10),
                        room(2L, "Large Computer Room", RoomType.COMPUTER_ROOM, 60, 30),
                        room(3L, "Amphitheater", RoomType.AMPHITHEATER, 200, null)
                ),
                10,
                criteria
        );

        assertThat(response.getRecommendedRoom().getName()).isEqualTo("Large Computer Room");
        assertThat(response.getAlternatives())
                .extracting(RoomDTO::getName)
                .containsExactly("Small Computer Room");
    }

    private RoomDTO room(Long id, String name, RoomType roomType, int capacity, Integer numberOfComputers) {
        return RoomDTO.builder()
                .id(id)
                .name(name)
                .roomType(roomType.getValue())
                .capacity(capacity)
                .numberOfComputers(numberOfComputers)
                .build();
    }

}
