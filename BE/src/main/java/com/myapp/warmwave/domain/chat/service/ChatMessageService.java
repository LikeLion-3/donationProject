package com.myapp.warmwave.domain.chat.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.domain.chat.dto.ChatMessageDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatMessageDto;
import com.myapp.warmwave.domain.chat.entity.ChatMessage;
import com.myapp.warmwave.domain.chat.entity.ChatRoom;
import com.myapp.warmwave.domain.chat.repository.ChatMessageRepository;
import com.myapp.warmwave.domain.chat.repository.ChatRoomRepository;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_CHATROOM;
import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository<User> userRepository;

    @Transactional
    public ResponseChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {

        User user = userRepository.findById(chatMessageDto.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_CHATROOM));

        ChatMessage chatMessage = ChatMessage.builder().chatroom(chatRoom).sender(user).message(chatMessageDto.getContent()).timestamp((new Date())).build();
        return ResponseChatMessageDto.fromEntity(chatMessageRepository.save(chatMessage));
    }

    public List<ResponseChatMessageDto> getAllChatMessages(Long roomId) {
        List<ChatMessage> chatHistoryList = chatMessageRepository.findAllByChatroomId(roomId);
        return chatHistoryList.stream().map(ResponseChatMessageDto::fromEntity).toList();
    }
}
