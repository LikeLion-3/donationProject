package com.myapp.warmwave.domain.chat.dto;

import com.myapp.warmwave.domain.chat.entity.ChatMessage;
import com.myapp.warmwave.domain.chat.entity.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResponseChatRoomDto {
    private Long id;

    private String donorName;

    private String recipientName;

    private String articleTitle;

    private String status;

    private String lastMessage;

    private LocalDateTime deletedAt;

    private Long articleId;

    public static ResponseChatRoomDto fromEntity(ChatRoom chatRoom) {
        List<ChatMessage> chatMessages = chatRoom.getChatMessageList();
        String lastMessageContent = chatMessages.isEmpty() ? "" : chatMessages.get(chatMessages.size() - 1).getMessage();
        return ResponseChatRoomDto.builder()
                .id(chatRoom.getId())
                .donorName(chatRoom.getDonor().getName())
                .recipientName(chatRoom.getRecipient().getName())
                .articleTitle(chatRoom.getArticle().getTitle())
                .status(chatRoom.getStatus())
                .lastMessage(lastMessageContent)
                .deletedAt(chatRoom.getDeletedAt())
                .articleId(chatRoom.getArticle().getId())
                .build();
    }

}
