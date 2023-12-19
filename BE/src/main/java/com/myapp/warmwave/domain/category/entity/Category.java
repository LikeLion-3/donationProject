package com.myapp.warmwave.domain.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myapp.warmwave.domain.article.entity.ArticleCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "TB_CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<ArticleCategory> articleCategories = new ArrayList<>();
}
