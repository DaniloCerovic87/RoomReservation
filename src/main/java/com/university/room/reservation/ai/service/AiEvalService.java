package com.university.room.reservation.ai.service;

import com.university.room.reservation.ai.dto.AiEvalRunResponse;

public interface AiEvalService {

    AiEvalRunResponse runBasicEvals(String username);

    AiEvalRunResponse runGuardrailEvals(String username);

    AiEvalRunResponse runReservationFlowEvals(String username);

    AiEvalRunResponse runAllEvals(String username);

}
