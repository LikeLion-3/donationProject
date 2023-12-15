package com.myapp.warmwave.domain.article.dto;

import com.myapp.warmwave.domain.article.entity.ArticleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Email
    private String userEmail;

    @NotNull
    private ArticleType articleType;

    private List<String> originalImageUrls;

    private List<MultipartFile> files;

    @NotNull
    @Size(min = 5, max = 50, message = "제목은 5자 이상 50자 이하로 입력해야 합니다.")
    private String title;

    @NotNull
    @Size(min = 10, max = 1000, message = "내용은 10자 이상 1000자 이하로 입력해야 합니다.")
    private String content;

    @NotNull
    private String prodCategory;
}
