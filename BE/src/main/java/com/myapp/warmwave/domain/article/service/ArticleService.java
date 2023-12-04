package com.myapp.warmwave.domain.article.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.main.dto.MainArticleDto;
import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.mapper.ArticleMapper;
import com.myapp.warmwave.domain.article.repository.ArticleRepository;
import com.myapp.warmwave.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ImageService imageService;

    public Article createArticle(ArticlePostDto dto, List<MultipartFile> imageFiles) throws IOException {

        Article article = articleMapper.articlePostDtoToArticle(dto);
        //추후 세터를 삭제하는 방향을 생각해보아야함
        article.setArticleImages(imageService.uploadImages(article, imageFiles));

        return articleRepository.save(article);
    }

    public Article getArticleByArticleId(long articleId) {
        return articleRepository.findById(articleId);
    }

    public Page<ArticleResponseDto> getAllArticles(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        List<ArticleResponseDto> articleResponseDtoList
                = articleRepository.findAll(pageRequest)
                .stream()
                .map(articleMapper::articleToArticleResponseDto)
                .toList();

        return new PageImpl<>(articleResponseDtoList);

    }

    public Article updateArticle(Long articleId, ArticlePostDto dto, List<MultipartFile> imageFiles) throws IOException {
        Article article = articleMapper.articlePostDtoToArticle(dto);
        Article findArticle = articleRepository.findById(articleId).orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));

        findArticle.applyPatch(article);
        findArticle.setArticleImages(imageService.uploadImages(findArticle, imageFiles));

        return articleRepository.save(findArticle);
    }

    public Page<MainArticleDto> findTop5OrderByCreatedAt(int num) {
        Pageable pageable = PageRequest.of(num, 5);
        return articleRepository.findTop5OrderByCreatedAtDesc(pageable);
    }
}
