package com.myapp.warmwave.domain.article.repository;

import com.myapp.warmwave.domain.article.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> {
    List<ArticleCategory> findByArticleId(long articleId);

    void deleteByArticleId(long articleId);
}
