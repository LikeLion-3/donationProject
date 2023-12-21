package com.myapp.warmwave.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.domain.comment.dto.CommentRequestDto;
import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import com.myapp.warmwave.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CommentController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                JwtAuthFilter.class
        })
})
@AutoConfigureRestDocs
@WithMockUser(roles = "USER")
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @DisplayName("댓글 작성 확인")
    @Test
    void create() throws Exception {
        // given
        Long communityId = 1L;
        CommentRequestDto reqDto = new CommentRequestDto("댓글 내용입니다. 글자수제한");
        String userEmail = "test@gmail.com";
        HttpServletRequest request = new MockHttpServletRequest();

        CommentResponseDto resDto = new CommentResponseDto(
                1L, "댓글 내용입니다. 글자수제한", LocalDateTime.now(), LocalDateTime.now(), "작성자", 1L, 1L
        );

        // when
        when(commentService.createComment(reqDto, communityId, userEmail, request)).thenReturn(resDto);

        // then
        mockMvc.perform(post("/api/communities/" + communityId + "/comments")
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("comment/댓글_작성"));
    }

    @DisplayName("댓글 목록 조회 확인")
    @Test
    void readAll() throws Exception {
        // given
        Long communityId = 1L;
        String sort = "popular";
        Pageable pageable = PageRequest.of(1, 12);

        Page<CommentResponseDto> dtoPage = new PageImpl<>(List.of(new CommentResponseDto(
                1L, "댓글 내용입니다. 글자수제한", LocalDateTime.now(), LocalDateTime.now(), "작성자", 1L, 1L
        )));

        // when
        when(commentService.getComments(any(), anyString(), any())).thenReturn(dtoPage);

        // then
        mockMvc.perform(get("/api/communities/" + communityId + "/comments")
                        .param("sort", sort)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment/댓글_목록_조회"));
    }

    @DisplayName("댓글 수정 확인")
    @Test
    void update() throws Exception {
        // given
        Long commentId = 1L;
        Long communityId = 1L;
        CommentRequestDto reqDto = new CommentRequestDto("댓글 내용 수정입니다. 글자수제한");
        String userEmail = "test@gmail.com";

        CommentResponseDto resDto = new CommentResponseDto(
                1L, "댓글 내용 수정입니다. 글자수제한", LocalDateTime.now(), LocalDateTime.now(), "작성자", 1L, 1L
        );

        // when
        when(commentService.updateComment(reqDto, commentId, communityId, userEmail)).thenReturn(resDto);

        // then
        mockMvc.perform(put("/api/communities/" + communityId + "/comments/" + commentId)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reqDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("comment/댓글_수정"));
    }

    @DisplayName("댓글 삭제 확인")
    @Test
    void delete() throws Exception {
        // given
        Long commentId = 1L;
        Long communityId = 1L;

        // when

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/communities/" + communityId + "/comments/" + commentId)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("comment/댓글_삭제"));
    }
}
