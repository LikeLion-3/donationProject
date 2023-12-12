package com.myapp.warmwave.domain.article.dto;

import com.myapp.warmwave.domain.article.entity.ArticleType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ArticlePostDto {
    @NotNull
    private String userEmail;

    @NotNull
    private ArticleType ArticleType;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String prodCategory;
}
