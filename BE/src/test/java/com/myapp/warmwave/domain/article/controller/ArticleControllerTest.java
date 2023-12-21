package com.myapp.warmwave.domain.article.controller;

import com.myapp.warmwave.common.jwt.JwtAuthFilter;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ArticleType;
import com.myapp.warmwave.domain.article.entity.Status;
import com.myapp.warmwave.domain.article.mapper.ArticleMapper;
import com.myapp.warmwave.domain.article.service.ArticleService;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ArticleController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                JwtAuthFilter.class
        })
})
@AutoConfigureRestDocs
@WithMockUser(roles = "USER")
class ArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private ArticleMapper articleMapper;

    @Test
    @DisplayName("게시글 작성 확인")
    void createArticle() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("imageFiles", "test-image.jpg", "image/jpeg", "image data".getBytes());
        String email = "test@gmail.com";
        String articleType = ArticleType.DONATION.getType();
        String title = "제목";
        String content = "내용";
        String prodCategories = "의류,잡화";

        Article article = Article.builder().id(1L).title(title).content(content).articleType(ArticleType.DONATION).build();

        ArticleResponseDto resDto = ArticleResponseDto.builder()
                .articleId(1L).userEmail(email).title(title).content(content).articleStatus("기본").articleType("의류,잡화").build();

        // when
        when(articleService.createArticle(any(), any(), any())).thenReturn(article);
        when(articleMapper.articleToArticleResponseDto(any())).thenReturn(resDto);

        // then
        mockMvc.perform(multipart("/api/articles")
                        .file(file)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("articleType", articleType)
                        .param("title", title)
                        .param("content", content)
                        .param("prodCategories", prodCategories)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("article/게시글_생성"));
    }

    @Test
    @DisplayName("게시글 목록 조회 확인")
    void readAllArticle() throws Exception {
        // given
        Integer page = 1;
        Integer size = 5;

        ArticleResponseDto resDto = ArticleResponseDto.builder()
                .articleId(1L).userEmail("test@gmail.com").title("제목").content("내용").articleStatus("기본").articleType("의류,잡화").build();

        Page<ArticleResponseDto> dtoPage = new PageImpl<>(List.of(resDto));

        // when
        when(articleService.getAllArticles(anyInt(), anyInt())).thenReturn(dtoPage);

        // then
        mockMvc.perform(get("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("article/게시글_목록_조회"));
    }

    @Test
    @DisplayName("게시글 단일 조회 확인")
    void readArticle() throws Exception {
        // given
        Long articleId = 1L;

        Article article = Article.builder().id(1L).title("제목").content("내용").articleType(ArticleType.DONATION).build();

        ArticleResponseDto resDto = ArticleResponseDto.builder()
                .articleId(1L).userEmail("test@gmail.com").title("제목").content("내용").articleStatus("기본").articleType("의류,잡화").build();

        // when
        when(articleService.createArticle(any(), any(), any())).thenReturn(article);
        when(articleMapper.articleToArticleResponseDto(any())).thenReturn(resDto);

        // then
        mockMvc.perform(get("/api/articles/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("article/게시글_단일_조회"));
    }

    // 파일 첨부 수정 테스트의 경우 PUT 요청을 할 수 없다. -> POST로 테스트 대체
    @Test
    @DisplayName("게시글 정보 수정 확인")
    void updateArticle() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("imageFiles", "test-image.jpg", "image/jpeg", "image data".getBytes());
        String email = "test@gmail.com";
        String articleType = ArticleType.DONATION.getType();
        String title = "제목 수정123";
        String content = "내용 수정 123";
        String prodCategories = "의류";

        Article article = Article.builder().id(1L).title(title).content(content).articleType(ArticleType.DONATION).build();

        ArticleResponseDto resDto = ArticleResponseDto.builder()
                .articleId(1L).userEmail(email).title(title).content(content).articleStatus("기본").articleType("의류").build();

        // when
        when(articleService.createArticle(any(), any(), any())).thenReturn(article);
        when(articleMapper.articleToArticleResponseDto(any())).thenReturn(resDto);

        // then
        mockMvc.perform(multipart("/api/articles")
                        .file(file)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("articleType", articleType)
                        .param("title", title)
                        .param("content", content)
                        .param("prodCategories", prodCategories)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("article/게시글_정보_수정"));
    }

    @Test
    @DisplayName("게시글 상태 변경 확인")
    void updateArticleStatus() throws Exception {
        // given
        Long articleId = 1L;
        String articleStatus = "진행중";
        String email = "test@gmail.com";

        Article article = Article.builder()
                .id(1L).title("제목").content("내용")
                .articleType(ArticleType.DONATION).articleStatus(Status.PROGRESS)
                .build();

        ArticleResponseDto resDto = ArticleResponseDto.builder()
                .articleId(1L).userEmail(email).title("제목").content("내용").articleStatus("진행중").articleType("의류").build();

        // when
        when(articleService.updateArticleStatus(any(), any(), any())).thenReturn(article);
        when(articleMapper.articleToArticleResponseDto(any())).thenReturn(resDto);

        // then
        mockMvc.perform(put("/api/articles/status/" + articleId)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("articleStatus", articleStatus)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("article/게시글_상태_변경"));
    }

    @Test
    @DisplayName("게시글 삭제 확인")
    void deleteArticle() throws Exception {
        // given
        Long articleId = 1L;

        // when

        // then
        mockMvc.perform(delete("/api/articles/" + articleId)
                        .header("Authorization", "Bearer $(ACCESS TOKEN)")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("article/게시글_삭제"));
    }
}
