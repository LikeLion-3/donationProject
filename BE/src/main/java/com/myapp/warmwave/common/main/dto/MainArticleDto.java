package com.myapp.warmwave.common.main.dto;

import lombok.Data;

@Data
public class MainArticleDto {
    private Long articleId;

    private String writer;

    private String title;

    private String content;

    private String type;

    private String status;

    private String createdAt;
}
