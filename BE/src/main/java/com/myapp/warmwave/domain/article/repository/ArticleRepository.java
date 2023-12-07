package com.myapp.warmwave.domain.article.repository;

import com.myapp.warmwave.common.main.dto.MainArticleDto;
import com.myapp.warmwave.domain.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByTitle(String title);

    @Query("SELECT new com.myapp.warmwave.common.main.dto.MainArticleDto(a.id, a.user.email, a.title, a.articleType, a.articleStatus, a.createdAt) " +
            "FROM Article a " +
            "WHERE DATE(a.createdAt) = CURRENT_DATE " +
            "ORDER BY a.createdAt DESC")
    Page<MainArticleDto> findTop5OrderByCreatedAtDesc(Pageable pageable);
}
