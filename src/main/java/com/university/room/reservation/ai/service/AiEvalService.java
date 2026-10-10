package com.university.room.reservation.ai.service;

import com.university.room.reservation.ai.dto.AiEvalRunResponse;

public interface AiEvalService {

    AiEvalRunResponse runBasicToolLogEvals(String username);

}
