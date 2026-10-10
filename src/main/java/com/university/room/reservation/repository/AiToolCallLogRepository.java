package com.university.room.reservation.repository;

import com.university.room.reservation.model.AiToolCallLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiToolCallLogRepository extends JpaRepository<AiToolCallLog, Long> {

    List<AiToolCallLog> findByConversationIdOrderByCreatedAtAsc(String conversationId);

}
