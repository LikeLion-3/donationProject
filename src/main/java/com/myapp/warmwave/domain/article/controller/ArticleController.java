package com.myapp.warmwave.domain.article.controller;


import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.mapper.ArticleMapper;
import com.myapp.warmwave.domain.article.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/article")
@Validated
@Slf4j
public class ArticleController {
    private ArticleService articleService;
    private ArticleMapper articleMapper;

    public ArticleController(ArticleService articleService, ArticleMapper articleMapper) {
        this.articleService = articleService;
        this.articleMapper = articleMapper;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity postArticle(@Validated @RequestPart ArticlePostDto dto,
                                      @RequestPart List<MultipartFile> imageFiles) throws IOException {

        Article article = articleService.create(dto, imageFiles);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

    @GetMapping("/{article-id}")
    public ResponseEntity getArticle(@PathVariable("article-id") long articleId) {

        Article article = articleService.getArticleByArticleId(articleId);

        return ResponseEntity.ok(articleMapper.articleToArticleResponseDto(article));
    }

}
