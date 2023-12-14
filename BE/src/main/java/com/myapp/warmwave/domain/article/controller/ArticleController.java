package com.myapp.warmwave.domain.article.controller;

import com.myapp.warmwave.common.main.dto.MainArticleDto;
import com.myapp.warmwave.domain.article.dto.ArticlePatchDto;
import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ArticleType;
import com.myapp.warmwave.domain.article.mapper.ArticleMapper;
import com.myapp.warmwave.domain.article.service.ArticleService;
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
    public ResponseEntity<ArticleResponseDto> postArticle(@AuthenticationPrincipal UserDetails userDetails,
                                                          String articleType,
                                                          List<MultipartFile> imageFiles,
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

        Article article = articleService.createArticle(dto, imageFiles);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

    @PatchMapping(value = "/{articleId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ArticleResponseDto> patchArticle(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable("articleId") Long articleId,
                                                           String articleType,
                                                           List<MultipartFile> imageFiles,
                                                           String title,
                                                           String content,
                                                           String prodCategories) throws IOException {
        ArticlePatchDto dto = ArticlePatchDto.builder()
                .articleId(articleId)
                .userEmail(userDetails.getUsername())
                .articleType(ArticleType.findArticleType(articleType))
                .files(imageFiles)
                .title(title)
                .content(content)
                .prodCategory(prodCategories)
                .build();

        Article article = articleService.updateArticle(userDetails.getUsername(), dto);

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
