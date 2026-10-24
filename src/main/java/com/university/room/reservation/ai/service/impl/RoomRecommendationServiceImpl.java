package com.university.room.reservation.ai.service.impl;

import com.university.room.reservation.ai.dto.RecommendationCriteria;
import com.university.room.reservation.ai.dto.RoomRecommendationResponse;
import com.university.room.reservation.ai.enums.CapacityPreference;
import com.university.room.reservation.ai.enums.ComputerRequirement;
import com.university.room.reservation.ai.service.RoomRecommendationService;
import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.model.enums.RoomType;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RoomRecommendationServiceImpl implements RoomRecommendationService {

    @Override
    public RoomRecommendationResponse recommendRoom(List<RoomDTO> availableRooms,
                                                    Integer requestedCapacity,
                                                    RecommendationCriteria criteria) {

        if (availableRooms == null || availableRooms.isEmpty()) {
            return RoomRecommendationResponse.builder()
                    .recommendedRoom(null)
                    .reason("No available rooms to recommend.")
                    .alternatives(List.of())
                    .build();
        }

        RecommendationCriteria effectiveCriteria = criteria == null
                ? RecommendationCriteria.builder().build()
                : criteria;
        CapacityPreference effectivePreference = effectiveCriteria.effectiveCapacityPreference();

        List<RoomDTO> suitableRooms = availableRooms.stream()
                .filter(room -> room.getCapacity() != null)
                .filter(room -> requestedCapacity == null || room.getCapacity() >= requestedCapacity)
                .filter(room -> matchesRoomType(room, effectiveCriteria.getPreferredRoomType()))
                .filter(room -> matchesComputerRequirement(
                        room,
                        requestedCapacity,
                        effectiveCriteria.effectiveComputerRequirement()
                ))
                .toList();

        if (suitableRooms.isEmpty()) {
            return RoomRecommendationResponse.builder()
                    .recommendedRoom(null)
                    .reason("No available rooms match the requested capacity and preferences.")
                    .alternatives(List.of())
                    .build();
        }

        List<RoomDTO> sortedRooms = sortRooms(suitableRooms, requestedCapacity, effectivePreference);

        RoomDTO recommendedRoom = sortedRooms.get(0);
        List<RoomDTO> alternatives = sortedRooms.stream()
                .skip(1)
                .toList();

        return RoomRecommendationResponse.builder()
                .recommendedRoom(recommendedRoom)
                .reason(buildReason(recommendedRoom, requestedCapacity, effectiveCriteria))
                .alternatives(alternatives)
                .build();
    }

    private boolean matchesRoomType(RoomDTO room, RoomType preferredRoomType) {
        return preferredRoomType == null || preferredRoomType.getValue().equalsIgnoreCase(room.getRoomType());
    }

    private boolean matchesComputerRequirement(RoomDTO room,
                                               Integer requestedCapacity,
                                               ComputerRequirement computerRequirement) {
        int numberOfComputers = room.getNumberOfComputers() == null ? 0 : room.getNumberOfComputers();

        return switch (computerRequirement) {
            case NONE -> true;
            case AT_LEAST_ONE -> numberOfComputers > 0;
            case ONE_PER_PARTICIPANT -> requestedCapacity != null && numberOfComputers >= requestedCapacity;
        };
    }

    private List<RoomDTO> sortRooms(
            List<RoomDTO> rooms,
            Integer requestedCapacity,
            CapacityPreference capacityPreference
    ) {
        return switch (capacityPreference) {
            case MOST_SPACIOUS -> rooms.stream()
                    .sorted(Comparator.comparing(RoomDTO::getCapacity).reversed())
                    .toList();
            case CLOSEST_MATCH -> rooms.stream()
                    .sorted(Comparator.comparingInt(room -> room.getCapacity() - requestedCapacity))
                    .toList();
        };
    }

    private String buildReason(RoomDTO room, Integer requestedCapacity, RecommendationCriteria criteria) {
        int unusedCapacity = room.getCapacity() - requestedCapacity;
        String capacityReason = switch (criteria.effectiveCapacityPreference()) {
            case MOST_SPACIOUS -> "%s is recommended because it is the most spacious available room for %d people, with %d extra seats."
                    .formatted(room.getName(), requestedCapacity, unusedCapacity);
            case CLOSEST_MATCH -> "%s is recommended because it has enough capacity for %d people with the smallest unused capacity of %d seats."
                    .formatted(room.getName(), requestedCapacity, unusedCapacity);
        };

        if (criteria.effectiveComputerRequirement() == ComputerRequirement.ONE_PER_PARTICIPANT) {
            return capacityReason + " It also has a computer for every participant.";
        }
        if (criteria.effectiveComputerRequirement() == ComputerRequirement.AT_LEAST_ONE) {
            return capacityReason + " It also satisfies the requested computer-room preference.";
        }
        if (criteria.getPreferredRoomType() != null) {
            return capacityReason + " It also matches the requested room type.";
        }
        return capacityReason;
    }

}
