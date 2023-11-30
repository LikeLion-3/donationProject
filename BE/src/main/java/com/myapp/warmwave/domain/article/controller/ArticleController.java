package com.myapp.warmwave.domain.article.controller;


import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.mapper.ArticleMapper;
import com.myapp.warmwave.domain.article.service.ArticleService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity postArticle(@Validated @RequestPart ArticlePostDto dto,
                                      @RequestPart List<MultipartFile> imageFiles) throws IOException {

        Article article = articleService.createArticle(dto, imageFiles);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity getArticle(@PathVariable("articleId") long articleId) {

        Article article = articleService.getArticleByArticleId(articleId);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

    @GetMapping
    public ResponseEntity<Page<Article>> getAllArticles(@Positive @RequestParam("page") int page,
                                                        @Positive @RequestParam("size") int size) {

        Page<Article> articles = articleService.getAllArticles(page, size);

        return ResponseEntity.ok(articles);
    }

    @PatchMapping(value = "/{articleId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity patchArticle(@PathVariable("articleId") Long articleId,
                                       @RequestPart ArticlePostDto dto,
                                       @RequestPart List<MultipartFile> imageFiles) throws IOException {

        Article article = articleService.updateArticle(articleId, dto, imageFiles);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

}