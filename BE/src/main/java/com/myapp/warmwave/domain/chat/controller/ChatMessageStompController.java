package com.myapp.warmwave.domain.chat.controller;

import com.myapp.warmwave.domain.chat.config.WebsocketUserContext;
import com.myapp.warmwave.domain.chat.dto.ChatMessageDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatMessageDto;
import com.myapp.warmwave.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ChatMessageStompController {

    private final SimpMessagingTemplate simpMessagingTemplate;  //특정 Broker로 메세지 전달

    private final ChatMessageService chatMessageService;

    @Autowired
    private final WebsocketUserContext websocketUserContext;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public ResponseChatMessageDto sendMsg(@Payload ChatMessageDto message, @DestinationVariable String roomId) {
        String username = websocketUserContext.getUserEmail();
        return chatMessageService.saveMessage(message, roomId, username);
    }
}
