package com.university.room.reservation.repository;

import com.university.room.reservation.model.AiConversation;
import com.university.room.reservation.model.AiConversationHistoryMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AiConversationHistoryMessageRepository extends JpaRepository<AiConversationHistoryMessage, Long> {

    Optional<AiConversationHistoryMessage> findTopByConversationOrderBySequenceNumberDesc(AiConversation conversation);

    List<AiConversationHistoryMessage> findByConversationOrderBySequenceNumberAsc(AiConversation conversation);
}
