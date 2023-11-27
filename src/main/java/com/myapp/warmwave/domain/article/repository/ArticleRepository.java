package com.myapp.warmwave.domain.article.repository;

import com.myapp.warmwave.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findById(long articleId);
}
