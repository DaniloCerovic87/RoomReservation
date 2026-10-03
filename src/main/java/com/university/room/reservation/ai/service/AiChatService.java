package com.university.room.reservation.ai.service;

import com.university.room.reservation.ai.request.AiChatRequest;

public interface AiChatService {

    String chat(AiChatRequest request, String username);

}
