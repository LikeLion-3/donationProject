package com.myapp.warmwave.domain.chat.repository;

import com.myapp.warmwave.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatroomId(Long roomId);

}
