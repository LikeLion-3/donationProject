package com.myapp.warmwave.domain.article.entity;

import com.myapp.warmwave.domain.category.entity.Category;
import jakarta.persistence.*;

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
}
