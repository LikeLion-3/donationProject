package com.myapp.warmwave.domain.article.controller;

import com.myapp.warmwave.common.main.dto.MainArticleDto;
import com.myapp.warmwave.domain.article.dto.ArticlePatchDto;
import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ArticleType;
import com.myapp.warmwave.domain.article.mapper.ArticleMapper;
import com.myapp.warmwave.domain.article.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticleResponseDto> postArticle(HttpServletRequest httpServletRequest,
                                                          @AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestParam(required = false) List<MultipartFile> imageFiles,
                                                          String articleType,
                                                          String title,
                                                          String content,
                                                          String prodCategories) throws IOException {
        ArticlePostDto dto = ArticlePostDto.builder()
                .userEmail(userDetails.getUsername())
                .ArticleType(ArticleType.findArticleType(articleType))
                .title(title)
                .content(content)
                .prodCategory(prodCategories)
                .build();

        Article article = articleService.createArticle(httpServletRequest, dto, imageFiles);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

    @PutMapping(value = "/{articleId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ArticleResponseDto> patchArticle(HttpServletRequest httpServletRequest,
                                                           @AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable("articleId") Long articleId,
                                                           @RequestParam(required = false) List<MultipartFile> imageFiles,
                                                           @RequestParam(required = false) List<String> originalImageUrls,
                                                           String articleType,
                                                           String title,
                                                           String content,
                                                           String prodCategories) throws Exception {
        ArticlePatchDto dto = ArticlePatchDto.builder()
                .articleId(articleId)
                .userEmail(userDetails.getUsername())
                .articleType(ArticleType.findArticleType(articleType))
                .originalImageUrls(originalImageUrls)
                .files(imageFiles)
                .title(title)
                .content(content)
                .prodCategory(prodCategories)
                .build();

        Article article = articleService.updateArticle(httpServletRequest, userDetails.getUsername(), dto);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

    @PutMapping("/status/{articleId}")
    public ResponseEntity<ArticleResponseDto> patchArticleStatus(@PathVariable("articleId") Long articleId,
                                                                 @AuthenticationPrincipal UserDetails userDetails,
                                                                 @RequestParam(required = true) String articleStatus) {

        Article article = articleService.updateArticleStatus(articleId, userDetails.getUsername(), articleStatus);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable("articleId") Long articleId) {

        Article article = articleService.getArticleByArticleId(articleId);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

    @GetMapping
    public ResponseEntity<Page<ArticleResponseDto>> getAllArticles(@Positive @RequestParam("page") Integer page,
                                                                   @Positive @RequestParam("size") Integer size) {

        return ResponseEntity.ok(articleService.getAllArticles(page, size));
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<Page<ArticleResponseDto>> getUsersAllArticles(@PathVariable("userId") Long userId,
                                                                        @Positive @RequestParam("page") Integer page,
                                                                        @Positive @RequestParam("size") Integer size) {
        return ResponseEntity.ok(articleService.getUserAllArticles(userId, page, size));
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity deleteArticle(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable("articleId") Long articleId) {
        articleService.deleteArticle(userDetails.getUsername(), articleId);
        return ResponseEntity.noContent().build();
    }

    // 최신 게시글 5개 조회
    @GetMapping("/today")
    public ResponseEntity<Page<MainArticleDto>> readTop5(
            @RequestParam(value = "num", defaultValue = "0") int pageNum
    ) {
        return ResponseEntity.ok(articleService.findTop5OrderByCreatedAt(pageNum));
    }
}
