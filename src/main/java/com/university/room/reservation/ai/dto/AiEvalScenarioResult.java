package com.university.room.reservation.ai.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AiEvalScenarioResult {

    private String scenarioId;

    private String scenarioName;

    private String conversationId;

    private boolean passed;

    private List<String> failures;

}
