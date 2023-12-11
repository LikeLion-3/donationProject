package com.myapp.warmwave.domain.chat.service;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.chat.dto.ChatMessageDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatMessageDto;
import com.myapp.warmwave.domain.chat.entity.ChatMessage;
import com.myapp.warmwave.domain.chat.entity.ChatRoom;
import com.myapp.warmwave.domain.chat.repository.ChatMessageRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {
    @Mock
    private ChatMessageRepository chatMessageRepository;
    
    @Mock
    private ChatRoomRepository chatRoomRepository;
    
    @Mock
    private UserRepository<User> userRepository;
    
    @InjectMocks
    private ChatMessageService chatMessageService;
    
    private Individual individual;
    private Institution institution;
    private ChatRoom chatRoom;
    private ChatMessage chatMessage;
    
    @BeforeEach
    void setup() {
        individual = Individual.builder()
                .id(1L).role(Role.INDIVIDUAL).build();

        institution = Institution.builder()
                .id(2L).role(Role.INSTITUTION).build();

        chatRoom = ChatRoom.builder()
                .id(1L)
                .donor(individual)
                .recipient(institution)
                .build();
        
        chatMessage = ChatMessage.builder()
                .id(1L)
                .chatroom(chatRoom)
                .sender(individual)
                .message("내용1")
                .build();
    }
    
    @DisplayName("채팅 메시지 저장 기능 확인")
    @Test
    void createMessage() {
        // given
        ChatMessageDto reqDto = saveChatMessage();

        // when
        ResponseChatMessageDto resDto = chatMessageService.saveMessage(reqDto);

        // then
        assertThat(resDto).isNotNull();
    }

    @DisplayName("채팅 메시지 목록 조회 기능 확인")
    @Test
    void readAllMessage() {
        // given
        ChatMessageDto reqDto = saveChatMessage();
        chatMessageService.saveMessage(reqDto);

        Long roomId = 1L;

        List<ChatMessage> messageList = List.of(chatMessage);
        when(chatMessageRepository.findAllByChatroomId(any())).thenReturn(messageList);
        
        // when
        List<ResponseChatMessageDto> resDtoList = chatMessageService.getAllChatMessages(roomId);
    
        // then
        assertThat(resDtoList).hasSameSizeAs(messageList);
    }

    private ChatMessageDto saveChatMessage() {
        ChatMessageDto reqDto = new ChatMessageDto("1", "내용1", "보낸 사람");

        // Authentication 은 어떻게 검증해야하지? 이렇게 하는거 맞나..?!
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                "test@gmail.com", "1234", null
        );

        SecurityContextHolder.getContext().setAuthentication(token);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(individual));
        when(chatRoomRepository.findById(any())).thenReturn(Optional.of(chatRoom));

        when(chatMessageRepository.save(any())).thenReturn(chatMessage);
        return reqDto;
    }
}
