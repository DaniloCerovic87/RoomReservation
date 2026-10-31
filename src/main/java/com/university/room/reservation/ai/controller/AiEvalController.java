package com.university.room.reservation.ai.controller;

import com.university.room.reservation.ai.dto.AiEvalRunResponse;
import com.university.room.reservation.ai.service.AiEvalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/evals")
public class AiEvalController {

    private final AiEvalService aiEvalService;

    @PostMapping("/basic")
    public AiEvalRunResponse runBasicEvals(Authentication authentication) {
        return aiEvalService.runBasicEvals(authentication.getName());
    }

    @PostMapping("/guardrails")
    public AiEvalRunResponse runGuardrailEvals(Authentication authentication) {
        return aiEvalService.runGuardrailEvals(authentication.getName());
    }

    @PostMapping("/reservation-flow")
    public AiEvalRunResponse runReservationFlowEvals(Authentication authentication) {
        return aiEvalService.runReservationFlowEvals(authentication.getName());
    }

    @PostMapping("/history")
    public AiEvalRunResponse runHistoryEvals(Authentication authentication) {
        return aiEvalService.runHistoryEvals(authentication.getName());
    }

    @PostMapping("/all")
    public AiEvalRunResponse runAllEvals(Authentication authentication) {
        return aiEvalService.runAllEvals(authentication.getName());
    }

}
