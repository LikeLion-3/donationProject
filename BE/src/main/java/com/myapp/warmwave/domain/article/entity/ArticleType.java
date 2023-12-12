package com.myapp.warmwave.domain.article.entity;


import com.myapp.warmwave.common.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_ARTICLETYPE;

@Getter
@RequiredArgsConstructor
public enum ArticleType {
    DONATION("기부해요"), BENEFICIARY("필요해요"), CERTIFICATION("인증해요");

    private final String type;

    public static ArticleType findArticleType(String input) {
        return Arrays.stream(values())
                .filter(articleType -> articleType.getType().equals(input))
                .findFirst()
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLETYPE));
    }
}
