package com.university.room.reservation.ai.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import com.university.room.reservation.ai.memory.JpaConversationHistoryService;
import com.university.room.reservation.ai.memory.JpaChatMemoryRepository;
import com.university.room.reservation.ai.memory.PersistentChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatMemory windowChatMemory(JpaChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();
    }

    @Bean
    public ChatMemory chatMemory(JpaConversationHistoryService historyService,
                                 @Qualifier("windowChatMemory") ChatMemory windowChatMemory) {
        return new PersistentChatMemory(historyService, windowChatMemory);
    }

}
