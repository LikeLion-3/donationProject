package com.myapp.warmwave.domain.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.config.security.SecurityConfig;
import com.myapp.warmwave.domain.chat.dto.ResponseChatMessageDto;
import com.myapp.warmwave.domain.chat.service.ChatMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ChatMessageApiController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthFilter.class})
})
public class ChatMessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;
    
    @DisplayName("채팅 메시지 저장 확인")
    @Test
    void saveChatMessage() throws Exception {
        // given
        Long roomId = 1L;
        ResponseChatMessageDto resDto = new ResponseChatMessageDto(
                1L, "보낸사람", "내용", "00"
        );

        List<ResponseChatMessageDto> dtoList = List.of(resDto);
        
        // when
        when(chatMessageService.getAllChatMessages(any())).thenReturn(dtoList);

        // then
        mockMvc.perform(get("/api/chatMessages/" + roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("test@gmail.com").password("1234").roles("INDIVIDUAL")))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
