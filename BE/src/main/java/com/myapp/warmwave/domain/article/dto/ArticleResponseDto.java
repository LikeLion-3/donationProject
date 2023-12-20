package com.myapp.warmwave.domain.article.dto;

import com.myapp.warmwave.domain.image.entity.Image;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleResponseDto {
    private Long articleId;

    private Long userId;

    private String userEmail;

    private String writer;

    private String title;

    private String content;

    private List<String> prodCategories;

    private String articleType;

    private String articleStatus;

    private List<Image> images;

    private String address;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
