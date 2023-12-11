package com.myapp.warmwave.domain.chat.service;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.repository.ArticleRepository;
import com.myapp.warmwave.domain.chat.dto.ChatRoomDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatRoomDto;
import com.myapp.warmwave.domain.chat.entity.ChatRoom;
import com.myapp.warmwave.domain.chat.repository.ChatRoomRepository;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository<User> userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    private Individual individual;
    private Institution institution;
    private Article article;
    private ChatRoom chatRoom;

    @BeforeEach
    void setup() {
        individual = Individual.builder()
                .id(1L).role(Role.INDIVIDUAL).build();

        institution = Institution.builder()
                .id(2L).role(Role.INSTITUTION).build();

        article = Article.builder()
                .id(1L).build();

        chatRoom = ChatRoom.builder()
                .id(1L)
                .donor(individual)
                .recipient(institution)
                .article(article)
                .chatMessageList(new ArrayList<>())
                .status("상태")
                .build();
    }

    @DisplayName("채팅방 생성 기능 확인")
    @Test
    void createChatRoom() {
        // given
        ChatRoomDto reqDto = saveChatRoom();

        // when
        ResponseChatRoomDto resDto = chatRoomService.createChatRoom(reqDto);

        // then
        assertThat(resDto).isNotNull();
    }

    @DisplayName("채팅방 목록 조회 기능 확인")
    @Test
    void readAllChatroom() {
        // given
        ChatRoomDto reqDto = saveChatRoom();
        chatRoomService.createChatRoom(reqDto);

        List<ChatRoom> chatRoomList = List.of(chatRoom);
        when(chatRoomRepository.findAll()).thenReturn(chatRoomList);

        // when
        List<ResponseChatRoomDto> resDtoList = chatRoomService.selectChatRoomList();

        // then
        assertThat(resDtoList).hasSameSizeAs(chatRoomList);
    }

    @DisplayName("채팅방 삭제 기능 확인")
    @Test
    void deleteChatroom() {
        // given
        ChatRoomDto reqDto = saveChatRoom();
        chatRoomService.createChatRoom(reqDto);

        Long roomId = 1L;
        when(chatRoomRepository.findById(any())).thenReturn(Optional.of(chatRoom));

        // when
        chatRoomService.deleteChatRoom(roomId);

        // then
        verify(chatRoomRepository, times(1)).findById(roomId);
        verify(chatRoomRepository, times(1)).delete(chatRoom);
    }

    private ChatRoomDto saveChatRoom() {
        ChatRoomDto reqDto = new ChatRoomDto(1L, 1L, 2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(institution));
        when(userRepository.findById(1L)).thenReturn(Optional.of(individual));
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));

        when(chatRoomRepository.save(any())).thenReturn(chatRoom);
        return reqDto;
    }
}
