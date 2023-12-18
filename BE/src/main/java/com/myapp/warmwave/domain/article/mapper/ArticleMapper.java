package com.myapp.warmwave.domain.article.mapper;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ArticleType;
import com.myapp.warmwave.domain.article.entity.Status;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_USER;
import static com.myapp.warmwave.common.exception.CustomExceptionCode.USER_ROLE_NOT_EXIST;

@Component
@RequiredArgsConstructor
public class ArticleMapper {
    private final UserRepository<User> userRepository;

    public Article articlePostDtoToArticle(ArticlePostDto dto) {

        Optional<User> user = userRepository.findByEmail(dto.getUserEmail());
        if(user.isEmpty()) {
            throw new CustomException(NOT_FOUND_USER);
        }

        switch (user.get().getRole()) {
            case INDIVIDUAL:
                if (dto.getArticleType() != ArticleType.DONATION) {
                    throw new CustomException(USER_ROLE_NOT_EXIST);
                }
                break;
            case INSTITUTION:
                if (dto.getArticleType() == ArticleType.DONATION) {
                    throw new CustomException(USER_ROLE_NOT_EXIST);
                }
                break;
        }

        return Article.builder()
                .user(user.get())
                .title(dto.getTitle())
                .content(dto.getContent())
                .articleType(dto.getArticleType())
                .articleStatus(Status.DEFAULT)
                .build();
    }

    public ArticleResponseDto articleToArticleResponseDto(Article article) {
        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .userId(article.getUser().getId())
                .userEmail(article.getUser().getEmail())
                .writer(article.getUser().getName())
                .title(article.getTitle())
                .content(article.getContent())
                .prodCategories(article.getArticleCategories().stream()
                        .map(articleCategory -> articleCategory.getCategory().getName())
                        .collect(Collectors.toList()))
                .articleType(article.getArticleType().getType())
                .articleStatus(article.getArticleStatus().getStatus())
                .images(article.getArticleImages())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .build();
    }
}
