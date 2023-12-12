package com.myapp.warmwave.domain.article.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myapp.warmwave.common.BaseEntity;
import com.myapp.warmwave.domain.article.dto.ArticlePostDto;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "TB_ARTICLE")
@EqualsAndHashCode
@ToString
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    //기부해요, 기부할래요, 인증할래요
    @Enumerated(EnumType.STRING)
    private ArticleType articleType;

    //게시글 상태(기본, 진행중, 완료)
    @Enumerated(EnumType.STRING)
    private Status articleStatus = Status.DEFAULT;

    private String userIp;

    private Long hit;

    @JsonIgnore
    @OneToMany(mappedBy = "article")
    private List<ArticleCategory> articleCategories = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    @JsonIgnore
    private List<Image> articleImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public void setArticleImages(List<Image> articleImages) {
        if (this.articleImages != null) {
            this.articleImages.clear();
        }
        this.articleImages = articleImages;
    }

    public void setArticleCategories(List<ArticleCategory> articleCategories) {
        this.articleCategories = articleCategories;
    }

    public void applyPatch(ArticlePostDto dto, List<ArticleCategory> articleCategories) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.articleCategories = articleCategories;
    }
}
