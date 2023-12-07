package com.myapp.warmwave.domain.article.mapper;

import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.Status;
import com.myapp.warmwave.domain.article.entity.Type;
import com.myapp.warmwave.domain.category.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    public Article articlePostDtoToArticle(ArticlePostDto articlePostDto, List<Category> categories) {
        return Article.builder()
                .title(articlePostDto.getTitle())
                .content(articlePostDto.getContent())
                .articleType(Type.DONATION)
                .articleStatus(Status.DEFAULT)
                .categories(categories)
                .build();
    }

    public ArticleResponseDto articleToArticleResponseDto(Article article) {
        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .writer("작성자") // 멤버 구현 후 리팩토링 필요
                .title(article.getTitle())
                .content(article.getContent())
                .prodCategories(article.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toList()))
                .articleType(article.getArticleType().toString())
                .articleStatus(article.getArticleStatus().toString())
                .images(article.getArticleImages())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .build();
    }
}
