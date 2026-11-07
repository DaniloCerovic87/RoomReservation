package com.university.room.reservation.ai.dto;

import com.university.room.reservation.ai.enums.CapacityPreference;
import com.university.room.reservation.ai.enums.ComputerRequirement;
import com.university.room.reservation.model.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationCriteria {

    private CapacityPreference capacityPreference;

    private RoomType preferredRoomType;

    private ComputerRequirement computerRequirement;

    public CapacityPreference effectiveCapacityPreference() {
        return capacityPreference == null ? CapacityPreference.CLOSEST_MATCH : capacityPreference;
    }

    public ComputerRequirement effectiveComputerRequirement() {
        return computerRequirement == null ? ComputerRequirement.NONE : computerRequirement;
    }

}
