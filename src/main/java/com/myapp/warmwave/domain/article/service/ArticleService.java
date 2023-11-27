package com.myapp.warmwave.domain.article.service;

import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.Status;
import com.myapp.warmwave.domain.article.entity.Type;
import com.myapp.warmwave.domain.article.entity.ProductCategory;
import com.myapp.warmwave.domain.article.repository.ArticleRepository;
import com.myapp.warmwave.domain.image.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ImageService imageService;

    public ArticleService (ArticleRepository articleRepository,
                           ImageService imageService) {
        this.articleRepository = articleRepository;
        this.imageService = imageService;
    }

    public Article create (ArticlePostDto articlePostDto, List<MultipartFile> imageFiles) throws IOException {

        Article article = Article.builder()
                .title(articlePostDto.getTitle())
                .content(articlePostDto.getContent())
                .articleType(Type.DONATION)
                .articleStatus(Status.DEFAULT)
                .prodCategory(ProductCategory.ETC)
                .build();

        //추후 세터를 삭제하는 방향을 생각해보아야함
        article.setArticleImages(imageService.upload(article, imageFiles));

        return articleRepository.save(article);
    }

    public Article getArticleByArticleId(long articleId) {
        return articleRepository.findById(articleId);
    }


}
