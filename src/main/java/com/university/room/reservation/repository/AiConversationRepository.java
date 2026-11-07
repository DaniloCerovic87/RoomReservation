package com.university.room.reservation.repository;

import com.university.room.reservation.model.AiConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiConversationRepository extends JpaRepository<AiConversation, Long> {

    Optional<AiConversation> findByConversationId(String conversationId);
}
