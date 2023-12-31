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
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.ArrayList;
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
    public Article createArticle(HttpServletRequest httpServletRequest, ArticlePostDto dto, List<MultipartFile> imageFiles) throws IOException {

        String userIp = getUserIP(httpServletRequest);
        List<Category> categories = categoryService.getCategory(dto.getProdCategory());
        Article article = articleMapper.articlePostDtoToArticle(userIp, dto);

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
    public Article updateArticle(HttpServletRequest httpServletRequest, String userEmail, ArticlePatchDto dto) throws Exception {

        String userIp = getUserIP(httpServletRequest);
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

        List<String> parsingOriginalUrls = new ArrayList<>();

        if (!dto.getOriginalImageUrls().get(0).equals("[]")){
            for(String imageUrl : dto.getOriginalImageUrls()) {
                parsingOriginalUrls.add(extractUrl(imageUrl));

            List<String> deleteImageUrls = findArticle.getArticleImages().stream()
                    .map(Image::getImgUrl)
                    .collect(Collectors.toList());

            deleteImageUrls.removeAll(parsingOriginalUrls);
            imageService.deleteImagesByUrls(deleteImageUrls);
            }
        }

        findArticle.applyPatch(userIp, dto, articleCategoryRepository.findByArticleId(findArticle.getId()));
        findArticle.setArticleImages(imageService.uploadImages(findArticle, dto.getFiles()));

        return articleRepository.save(findArticle);
    }

    @Transactional
    public Article updateArticleStatus(Long articleId, String userEmail, String articleStatus) {
        Article findArticle = getArticleByArticleId(articleId);

        findArticle.setArticleStatusByString(articleStatus);

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

    public Page<ArticleResponseDto> getUserAllArticles(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        List<ArticleResponseDto> articleResponseDtoList
                = articleRepository.findByUserId(userId, pageRequest)
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

        if (articleRepository.existsById(articleId)) {
            throw new CustomException(FAILED_TO_REMOVE);
        }
    }

    public Page<MainArticleDto> findTop5OrderByCreatedAt(int num) {
        Pageable pageable = PageRequest.of(num, 5);
        return articleRepository.findTop5OrderByCreatedAtDesc(pageable);
    }

    private String getUserIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private static String extractUrl(String jsonString) throws Exception {
        int startIndex = jsonString.indexOf("\"") + 1;
        int endIndex = jsonString.lastIndexOf("\"");

        if (startIndex > 0 && endIndex > startIndex) {
            return jsonString.substring(startIndex, endIndex);
        } else {
            throw new CustomException(INVALID_IMAGE_URL);
        }
    }
}
