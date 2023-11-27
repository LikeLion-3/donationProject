package com.myapp.warmwave.domain.image.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myapp.warmwave.domain.article.entity.Article;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "TB_IMAGE")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;

    private String imgUrl;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ARTICLE_ID")
    @JsonIgnore
    private Article article;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public void update(String imgName, String imgUrl) {
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
