package com.university.room.reservation.ai.agent;

import com.university.room.reservation.ai.dto.RecommendationCriteria;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RecommendationAgent {

    private final ChatClient chatClient;

    public RecommendationAgent(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                        You interpret a user's room preferences and return structured recommendation criteria.

                        Capacity preference:
                        - MOST_SPACIOUS only when the user explicitly asks for a larger, spacious, comfortable,
                          roomy, biggest, or not-too-tight room.
                        - CLOSEST_MATCH when the user asks for the smallest suitable room, closest capacity match,
                          least unused capacity, says that a large room is unnecessary, or gives no preference.

                        Preferred room type:
                        - COMPUTER_ROOM when the user explicitly asks for a computer room or computers.
                        - AMPHITHEATER when the user explicitly asks for an amphitheater.
                        - CLASSROOM when the user explicitly asks for a classroom.
                        - null when no room type is requested.

                        Computer requirement:
                        - ONE_PER_PARTICIPANT only when the user explicitly requires a computer for every participant.
                        - AT_LEAST_ONE when computers or a computer room are requested without requiring one per person.
                        - NONE when computers are not requested.

                        Serbian examples:
                        - "komfornija sala", "da ne bude knap" -> MOST_SPACIOUS
                        - "najmanja odgovarajuca", "ne treba mi velika sala" -> CLOSEST_MATCH
                        - "racunar za svakoga" -> COMPUTER_ROOM and ONE_PER_PARTICIPANT
                        - "racunarska sala" -> COMPUTER_ROOM and AT_LEAST_ONE

                        Do not invent preferences that the user did not express.
                        """)
                .build();
    }

    public RecommendationCriteria interpret(String userMessage) {
        return chatClient
                .prompt()
                .user(userMessage)
                .call()
                .entity(RecommendationCriteria.class);
    }

}
