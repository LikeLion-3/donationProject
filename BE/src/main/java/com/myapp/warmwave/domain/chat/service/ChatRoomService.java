package com.myapp.warmwave.domain.chat.service;

import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.repository.ArticleRepository;
import com.myapp.warmwave.domain.chat.dto.ChatRoomDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatRoomDto;
import com.myapp.warmwave.domain.chat.dto.ResponseCreateChatRoomDto;
import com.myapp.warmwave.domain.chat.entity.ChatRoom;
import com.myapp.warmwave.domain.chat.repository.ChatRoomRepository;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository<User> userRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public ResponseCreateChatRoomDto createChatRoom(ChatRoomDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        User otherUser = userRepository.findById(requestDto.getOtherId())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        Article article = articleRepository.findById(requestDto.getArticleId()).orElseThrow();

        ChatRoom chatRoom = ChatRoom.builder().donor(user).recipient(otherUser).article(article).build();

        return ResponseCreateChatRoomDto.fromEntity(chatRoomRepository.save(chatRoom));
    }

    public List<ResponseChatRoomDto> selectChatRoomList() {
        return chatRoomRepository.findAll()
                .stream()
                .map(ResponseChatRoomDto::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다"));

        chatRoomRepository.delete(chatRoom);
    }
}
