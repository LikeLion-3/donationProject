package com.myapp.warmwave.common.main.dto;

import com.myapp.warmwave.domain.article.entity.ProductCategory;
import com.myapp.warmwave.domain.article.entity.Status;
import com.myapp.warmwave.domain.article.entity.Type;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainArticleDto {
    private Long articleId;

    private String writer;

    private String title;

    private Type type;

    private Status status;

//    private ProductCategory category;

    private LocalDateTime createdAt;

    public MainArticleDto(Long articleId, String writer, String title, Type type, Status status, LocalDateTime createdAt) {
        this.articleId = articleId;
        this.writer = writer;
        this.title = title;
        this.type = type;
        this.status = status;
//        this.category = category;
        this.createdAt = createdAt;
    }
}
