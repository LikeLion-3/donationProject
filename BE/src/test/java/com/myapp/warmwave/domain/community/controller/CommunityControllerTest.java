package com.myapp.warmwave.domain.community.controller;

import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.service.CommunityFacadeService;
import com.myapp.warmwave.domain.community.service.CommunityService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CommunityController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                JwtAuthFilter.class
        })
})
@AutoConfigureRestDocs
@WithMockUser(roles = "USER")
class CommunityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityService communityService;

    @MockBean
    private CommunityFacadeService communityFacadeService;

    @DisplayName("커뮤니티 게시글 작성 확인")
    @Test
    void create() throws Exception {
        // given
        CommunityPostDto reqDto = CommunityPostDto.builder()
                .title("제목입니다").contents("내용입니다. 길게 써야 하네요 하하").category("카테고리").build();

        List<MockMultipartFile> images = Collections.singletonList(new MockMultipartFile("images", "test-image.jpg", "image/jpeg", "image data".getBytes()));

        CommunityResponseDto resDto = CommunityResponseDto.builder()
                .id(1L).title("제목입니다").contents("내용입니다. 길게 써야 하네요 하하").userId(1L).category("카테고리")
                .writer("작성자").build();

        // when
        when(communityFacadeService.createCommunity(any(), any(), any(), any())).thenReturn(resDto);

        // then
        mockMvc.perform(multipart("/api/communities")
                        .file(images.get(0))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .param("title", reqDto.getTitle())
                        .param("contents", reqDto.getContents())
                        .param("category", reqDto.getCategory())
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("community/게시글_생성"));
    }

    @DisplayName("커뮤니티 게시글 목록 조회 확인")
    @Test
    void readAll() throws Exception {
        // given
        String sort = "popular";
        Pageable pageable = PageRequest.of(1, 12);

        CommunityListResponseDto resDto = CommunityListResponseDto.builder()
                .id(1L).title("제목").hit(0).writer("작성자").category("카테고리").build();

        Page<CommunityListResponseDto> responseDtoPage = new PageImpl<>(List.of(resDto));

        // when
        when(communityService.getAllCommunities(pageable, sort)).thenReturn(responseDtoPage);

        // then
        mockMvc.perform(get("/api/communities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("community/게시글_목록_조회"));
    }

    @DisplayName("커뮤니티 게시글 조회 확인")
    @Test
    void read() throws Exception {
        // given
        Long communityId = 1L;

        CommunityResponseDto resDto = CommunityResponseDto.builder()
                .id(1L).title("제목").contents("내용").userId(1L).category("카테고리")
                .writer("작성자").build();

        // when
        when(communityService.getCommunity(any())).thenReturn(resDto);

        // then
        mockMvc.perform(get("/api/communities/" + communityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("community/게시글_단일_조회"));
    }

    // 이미지가 첨부된 게시글 수정의 경우 테스트코드 단에서 put 을 사용하여 처리하기 어려움
    // 그래서 이미지 수정을 하지 않고 진행
    @DisplayName("커뮤니티 게시글 수정 확인")
    @Test
    void update() throws Exception {
        // given
        Long communityId = 1L;

        CommunityPatchDto reqDto = CommunityPatchDto.builder()
                .title("제목 수정입니다").contents("내용 수정입니다. 길게 써야 하네요 하하").category("카테고리 수정").build();

        MockMultipartFile images = new MockMultipartFile("images", "test-image.jpg", "image/jpeg", "image data".getBytes());

        CommunityResponseDto resDto = CommunityResponseDto.builder()
                .id(1L).title("제목 수정입니다").contents("내용 수정입니다. 길게 써야 하네요 하하").userId(1L).category("카테고리")
                .writer("작성자").build();

        // when
        when(communityService.updateCommunity(any(), any(), any(), any(), any())).thenReturn(resDto);

        // then
        mockMvc.perform(put("/api/communities/" + communityId)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("title", reqDto.getTitle())
                        .param("contents", reqDto.getContents())
                        .param("category", reqDto.getCategory())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("community/게시글_수정"));
    }

    @DisplayName("커뮤니티 게시글 삭제 확인")
    @Test
    void delete() throws Exception {
        // given
        Long communityId = 1L;

        // when

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/communities/" + communityId)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("community/게시글_삭제"));
    }
}
