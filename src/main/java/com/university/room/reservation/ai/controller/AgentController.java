package com.university.room.reservation.ai.controller;

import com.university.room.reservation.ai.request.AiChatRequest;
import com.university.room.reservation.ai.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AgentController {

    private final AiChatService aiChatService;

    @PostMapping("/chat")
    public String chat(@RequestBody AiChatRequest request, Authentication authentication) {
        return aiChatService.chat(request, authentication.getName());
    }
}
