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

    @PostMapping("/basic-tool-logs")
    public AiEvalRunResponse runBasicToolLogEvals(Authentication authentication) {
        return aiEvalService.runBasicToolLogEvals(authentication.getName());
    }

}
