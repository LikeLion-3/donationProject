package com.myapp.warmwave.domain.article.repository;

import com.myapp.warmwave.common.main.dto.MainArticleDto;
import com.myapp.warmwave.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findById(long articleId);

    @Query("SELECT new com.myapp.warmwave.common.main.dto.MainArticleDto(a.id, a.user.email, a.title, a.articleType, a.articleStatus, a.prodCategory, a.createdAt) " +
            "FROM Article a " +
            "ORDER BY a.createdAt DESC")
    List<MainArticleDto> findTop5OrderByCreatedAtDesc();
}
