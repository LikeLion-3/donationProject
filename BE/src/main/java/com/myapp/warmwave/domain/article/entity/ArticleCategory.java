package com.myapp.warmwave.domain.article.entity;

import com.myapp.warmwave.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "article_category")
@Entity
public class ArticleCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public static ArticleCategory buildArticleCategory (Article article, Category category) {
        return ArticleCategory.builder()
                .article(article)
                .category(category)
                .build();
    }

}
