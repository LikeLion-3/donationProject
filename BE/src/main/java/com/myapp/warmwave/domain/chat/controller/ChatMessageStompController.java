package com.myapp.warmwave.domain.chat.controller;

import com.myapp.warmwave.domain.chat.dto.ChatMessageDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatMessageDto;
import com.myapp.warmwave.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ChatMessageStompController {

    private final SimpMessagingTemplate simpMessagingTemplate;  //특정 Broker로 메세지 전달

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public ResponseChatMessageDto sendMsg(ChatMessageDto message, Principal principal) {
        chatMessageService.saveMessage(message, principal.getName());
        return chatMessageService.saveMessage(message, principal.getName());
    }

}
