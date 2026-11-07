package com.university.room.reservation.ai.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiChatRequest {

    private String conversationId;

    private String message;
}