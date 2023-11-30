package com.myapp.warmwave.domain.article.mapper;

import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ProductCategory;
import com.myapp.warmwave.domain.article.entity.Status;
import com.myapp.warmwave.domain.article.entity.Type;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public Article articlePostDtoToArticle(ArticlePostDto articlePostDto) {
        return Article.builder()
                .title(articlePostDto.getTitle())
                .content(articlePostDto.getContent())
                .articleType(Type.DONATION)
                .articleStatus(Status.DEFAULT)
                .prodCategory(ProductCategory.ETC)
                .build();
    }

    public ArticleResponseDto articleToArticleResponseDto(Article article) {
        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .writer(article.getUser().getEmail()) // 멤버 구현 후 리팩토링 필요
                .title(article.getTitle())
                .content(article.getContent())
                .prodCategory(article.getProdCategory().toString())
                .articleType(article.getArticleType().toString())
                .articleStatus(article.getArticleStatus().toString())
                .images(article.getArticleImages())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .build();
    }
}
