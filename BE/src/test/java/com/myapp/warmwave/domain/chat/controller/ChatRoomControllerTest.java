package com.myapp.warmwave.domain.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.warmwave.WithMockCustomUser;
import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.config.security.SecurityConfig;
import com.myapp.warmwave.domain.chat.dto.ChatRoomDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatRoomDto;
import com.myapp.warmwave.domain.chat.dto.ResponseCreateChatRoomDto;
import com.myapp.warmwave.domain.chat.service.ChatRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ChatRoomController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, JwtAuthFilter.class})
})
@WithMockUser(roles = "USER")
@AutoConfigureRestDocs
class ChatRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    @DisplayName("채팅방 생성 확인")
    @Test
    @WithMockCustomUser
    void createChatroom() throws Exception {
        // given
        ChatRoomDto reqDto = new ChatRoomDto(1L, 1L);
        ResponseCreateChatRoomDto resDto = new ResponseCreateChatRoomDto(
                1L, "기부자1", "수여자1", "제목", "상태", "메시지", null
        );

        // when
        when(chatRoomService.createChatRoom(any(), any())).thenReturn(resDto);

        // then
        mockMvc.perform(post("/api/chatRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("chat/채팅방_생성"));
    }

    @DisplayName("채팅방 목록 조회 확인")
    @Test
    void readAllChatroom() throws Exception {
        // given
        ResponseChatRoomDto resDto = new ResponseChatRoomDto(
                1L, "기부자1", "수여자1", "제목", "상태", "메시지", null, 1L
        );

        List<ResponseChatRoomDto> chatRoomDtoList = List.of(resDto);

        // when
        when(chatRoomService.selectChatRoomList()).thenReturn(chatRoomDtoList);

        // then
        mockMvc.perform(get("/api/chatRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("chat/채팅방_목록_조회"));
    }

    @DisplayName("채팅방 삭제 확인")
    @Test
    void delete() throws Exception {
        // given
        Long roomId = 1L;

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/chatRoom/" + roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("chat/채팅방_삭제"));
    }
}
