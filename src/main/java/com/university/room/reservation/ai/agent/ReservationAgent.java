package com.university.room.reservation.ai.agent;

import com.university.room.reservation.ai.tool.ReservationTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ReservationAgent {

    private final ChatClient chatClient;

    public ReservationAgent(ChatClient.Builder builder, ReservationTools reservationTools) {
        this.chatClient = builder
                .defaultSystem("""
                  You are an AI assistant for a meeting room reservation application.

                  You help users find available meeting rooms.

                  Use tools when the user asks to find available rooms and provides:
                  - date
                  - start time
                  - end time
                  - capacity

                  Do not invent missing information.
                  If any required information is missing, ask a concise follow-up question.

                  Do not create, update, or cancel reservations.
                  For now, you can only help users search for available rooms.
                  """)
                .defaultTools(reservationTools)
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