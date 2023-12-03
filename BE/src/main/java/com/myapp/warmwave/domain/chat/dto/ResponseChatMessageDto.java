package com.myapp.warmwave.domain.chat.dto;

import com.myapp.warmwave.domain.chat.entity.ChatMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResponseChatMessageDto {
    private Long id;

    private String nickName;

    private String message;

    private String timestamp;

    public static ResponseChatMessageDto fromEntity(ChatMessage chatMessage) {
        return ResponseChatMessageDto.builder()
                .id(chatMessage.getId())
                .nickName(chatMessage.getChatroom().getDonor().getNickname())
                .message(chatMessage.getMessage())
                .build();
    }
}
