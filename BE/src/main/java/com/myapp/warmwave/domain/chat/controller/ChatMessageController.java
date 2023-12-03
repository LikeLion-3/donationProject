package com.myapp.warmwave.domain.chat.controller;

import com.myapp.warmwave.domain.chat.dto.ChatMessageDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatMessageDto;
import com.myapp.warmwave.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;  //특정 Broker로 메세지 전달

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void sendMsg(ChatMessageDto message) {
        simpMessagingTemplate.convertAndSend("/topic/messages/" + message.getRoomId(), message);
    }

    @MessageMapping("/history/{roomId}")
    public void getChatHistory(@DestinationVariable String roomId) {
        List<ResponseChatMessageDto> chatHistory = chatMessageService.getChatHistory(roomId);
        simpMessagingTemplate.convertAndSend("/topic/history/" + roomId, chatHistory);
    }

}
