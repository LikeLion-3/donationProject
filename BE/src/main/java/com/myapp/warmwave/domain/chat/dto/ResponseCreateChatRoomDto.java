package com.myapp.warmwave.domain.chat.dto;

import com.myapp.warmwave.domain.chat.entity.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResponseCreateChatRoomDto {
    private Long id;

    private String donorName;

    private String recipientName;

    private String articleTitle;

    private String status;

    private String lastMessage;

    private LocalDateTime deletedAt;

    public static ResponseCreateChatRoomDto fromEntity(ChatRoom chatRoom) {
        return ResponseCreateChatRoomDto.builder()
                .id(chatRoom.getId())
                .donorName(chatRoom.getDonor().getNickname())
                .recipientName(chatRoom.getRecipient().getInstitutionName())
                .articleTitle(chatRoom.getArticle().getTitle())
                .status(chatRoom.getStatus())
                .deletedAt(chatRoom.getDeletedAt())
                .build();
    }
}
