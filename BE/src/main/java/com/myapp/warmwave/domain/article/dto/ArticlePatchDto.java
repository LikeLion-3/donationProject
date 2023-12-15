package com.myapp.warmwave.domain.article.dto;

import com.myapp.warmwave.domain.article.entity.ArticleType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class ArticlePatchDto {
    @NotNull
    private Long articleId;

    @NotNull
    private String userEmail;

    @NotNull
    private ArticleType articleType;

    private List<String> originalImageUrls;

    private List<MultipartFile> files;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String prodCategory;
}
