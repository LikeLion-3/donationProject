package com.myapp.warmwave.domain.article.dto;

import com.myapp.warmwave.domain.article.entity.Status;
import com.myapp.warmwave.domain.article.entity.Type;
import com.myapp.warmwave.domain.image.entity.Image;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ArticleResponseDto {
    private long articleId;

    //추후 멤버객체로 매핑예정
    private String writer;

    private String title;

    private String content;

    private Type articleType;

    private Status articleStatus;

    private List<Image> images;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
