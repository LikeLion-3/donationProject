package com.myapp.warmwave.domain.chat.controller;

import com.myapp.warmwave.domain.chat.dto.ChatMessageDto;
import com.myapp.warmwave.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageStompController {

    private final SimpMessagingTemplate simpMessagingTemplate;  //특정 Broker로 메세지 전달

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void sendMsg(ChatMessageDto message) {
        chatMessageService.saveMessage(message);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + message.getRoomId(), message);
    }

}
