package com.university.room.reservation.ai.controller;

import com.university.room.reservation.ai.agent.ReservationAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AgentController {

    private final ReservationAgent reservationAgent;

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        return reservationAgent.chat(message);
    }
}