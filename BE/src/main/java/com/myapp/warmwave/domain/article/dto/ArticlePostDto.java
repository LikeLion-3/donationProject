package com.myapp.warmwave.domain.article.dto;

import com.myapp.warmwave.domain.article.entity.ArticleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ArticlePostDto {
    @NotNull
    @Email
    private String userEmail;

    @NotNull
    private ArticleType ArticleType;

    @NotNull
    @Size(min = 5, max = 50, message = "제목은 5자 이상 50자 이하로 입력해야 합니다.")
    private String title;

    @NotNull
    @Size(min = 10, max = 1000, message = "내용은 10자 이상 1000자 이하로 입력해야 합니다.")
    private String content;

    @NotNull
    private String prodCategory;
}
