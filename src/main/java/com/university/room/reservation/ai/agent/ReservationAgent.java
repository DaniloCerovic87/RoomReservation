package com.university.room.reservation.ai.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ReservationAgent {

    private final ChatClient chatClient;

    public ReservationAgent(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                    You are an AI assistant for a meeting room
                    reservation application.

                    Help users find and reserve meeting rooms.
                    Be concise and helpful.
                    """)
                .build();
    }

    public String chat(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }
}