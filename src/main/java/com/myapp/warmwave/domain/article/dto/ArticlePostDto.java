package com.myapp.warmwave.domain.article.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ArticlePostDto {
    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String prodCategory;
}
