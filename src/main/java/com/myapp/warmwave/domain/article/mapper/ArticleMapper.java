package com.myapp.warmwave.domain.article.mapper;

import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public ArticleResponseDto articleToArticleResponseDto(Article article) {
        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .writer("작성자") // 멤버 구현 후 리팩토링 필요
                .title(article.getTitle())
                .content(article.getContent())
                .articleType(article.getArticleType())
                .articleStatus(article.getArticleStatus())
                .images(article.getArticleImages())
                .createdAt(article.getCreatedAt())
                .build();
    }
}
