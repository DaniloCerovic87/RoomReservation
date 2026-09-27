package com.university.room.reservation.ai.service;

import com.university.room.reservation.ai.agent.ReservationAgent;
import com.university.room.reservation.ai.request.AiChatRequest;
import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ResourceNotFoundException;
import com.university.room.reservation.model.User;
import com.university.room.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatService {

    private final ReservationAgent reservationAgent;
    private final UserRepository userRepository;

    public String chat(AiChatRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageProperties.USER_NOT_FOUND));

        return reservationAgent.chat(request.getConversationId(), request.getMessage(), user.getId());
    }

}
