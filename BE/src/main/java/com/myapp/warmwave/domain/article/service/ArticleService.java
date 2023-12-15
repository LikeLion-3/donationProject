package com.myapp.warmwave.domain.article.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.main.dto.MainArticleDto;
import com.myapp.warmwave.domain.article.dto.ArticlePatchDto;
import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.dto.ArticleResponseDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ArticleCategory;
import com.myapp.warmwave.domain.article.mapper.ArticleMapper;
import com.myapp.warmwave.domain.article.repository.ArticleCategoryRepository;
import com.myapp.warmwave.domain.article.repository.ArticleRepository;
import com.myapp.warmwave.domain.category.entity.Category;
import com.myapp.warmwave.domain.category.service.CategoryService;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.image.service.ImageService;
import com.myapp.warmwave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.*;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final ArticleCategoryRepository articleCategoryRepository;

    @Transactional
    public Article createArticle(ArticlePostDto dto, List<MultipartFile> imageFiles) throws IOException {

        List<Category> categories = categoryService.getCategory(dto.getProdCategory());
        Article article = articleMapper.articlePostDtoToArticle(dto);

        //추후 세터를 삭제하는 방향을 생각해보아야함
        // 로직에 fileName 필요 없음 -> fileName에 article 정보 저장하면 단방향 매핑 가능하지 않을까?
        //  - 이러면 uploadImages 메서드의 파라미터로 article 엔티티 통째로 대신 article 정보(ex. id나 name)만 써도 됨
        article.setArticleImages(imageService.uploadImages(article, imageFiles));
        Article savedArticle = articleRepository.save(article);

        for (Category category : categories) {
            ArticleCategory articleCategory = ArticleCategory.ofArticleCategory(savedArticle, category);
            articleCategoryRepository.save(articleCategory);
        }

        savedArticle.setArticleCategories(articleCategoryRepository.findByArticleId(savedArticle.getId()));

        return savedArticle;
    }

    @Transactional
    public Article updateArticle(String userEmail, ArticlePatchDto dto) throws IOException {

        Article findArticle = getArticleByArticleId(dto.getArticleId());
        User user = findArticle.getUser();

        if (!user.getEmail().equals(userEmail)) {
            throw new CustomException(USER_ROLE_NOT_EXIST);
        }

        articleCategoryRepository.deleteByArticleId(findArticle.getId());

        List<Category> categories = categoryService.getCategory(dto.getProdCategory());

        for (Category category : categories) {
            ArticleCategory articleCategory = ArticleCategory.ofArticleCategory(findArticle, category);
            articleCategoryRepository.save(articleCategory);
        }

        //추후 세터를 삭제하는 방향을 생각해보아야함
        List<String> deleteImageUrls = findArticle.getArticleImages().stream()
                .map(Image::getImgUrl)
                .filter(url -> !dto.getOriginalImageUrls().contains(url))
                .collect(Collectors.toList());

        imageService.deleteImagesByUrls(deleteImageUrls);
        findArticle.applyPatch(dto, articleCategoryRepository.findByArticleId(findArticle.getId()));
        findArticle.setArticleImages(imageService.uploadImages(findArticle, dto.getFiles()));

        return articleRepository.save(findArticle);
    }

    public Article getArticleByArticleId(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));
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

    @Transactional
    public void deleteArticle(String userName, long articleId) {
        Article article = getArticleByArticleId(articleId);
        User user = article.getUser();

        if (!user.getEmail().equals(userName)) {
            throw new CustomException(USER_ROLE_NOT_EXIST);
        }

        imageService.deleteImagesByArticleId(articleId);
        articleCategoryRepository.deleteByArticleId(articleId);
        articleRepository.deleteById(articleId);

        if(articleRepository.existsById(articleId)) {
            new CustomException(FAILED_TO_REMOVE);
        }
    }



    public Page<MainArticleDto> findTop5OrderByCreatedAt(int num) {
        Pageable pageable = PageRequest.of(num, 5);
        return articleRepository.findTop5OrderByCreatedAtDesc(pageable);
    }
}
