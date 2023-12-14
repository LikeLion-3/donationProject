package com.myapp.warmwave.domain.chat.service;

import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.repository.ArticleRepository;
import com.myapp.warmwave.domain.chat.dto.ChatRoomDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatRoomDto;
import com.myapp.warmwave.domain.chat.dto.ResponseCreateChatRoomDto;
import com.myapp.warmwave.domain.chat.entity.ChatRoom;
import com.myapp.warmwave.domain.chat.repository.ChatRoomRepository;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
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
    public ResponseCreateChatRoomDto createChatRoom(ChatRoomDto requestDto) {
        Institution recipient = userRepository.findById(requestDto.getRecipientId())
                .map(Institution.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재 하지 않습니다"));

        Individual doner = userRepository.findById(requestDto.getDonerId())
                .map(Individual.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재 하지 않습니다"));

        Article article = articleRepository.findById(requestDto.getArticleId()).orElseThrow();

        ChatRoom chatRoom = ChatRoom.builder().donor(doner).recipient(recipient).article(article).build();

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
