package com.university.room.reservation.ai.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AiEvalRunResponse {

    private int total;

    private int passed;

    private int failed;

    private List<AiEvalScenarioResult> results;

}
