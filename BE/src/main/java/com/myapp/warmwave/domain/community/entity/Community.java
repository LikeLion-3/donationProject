package com.myapp.warmwave.domain.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myapp.warmwave.common.BaseEntity;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.user.entity.User;
import jakarta.persistence.*;
import com.myapp.warmwave.domain.comment.entity.Comment;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Table(name="TB_COMMUNITY")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Community extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String contents;

    private String category;

    @Column(name = "user_ip")
    private String userIp;

    private Integer hit;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.hit = this.hit == null ? 0 : this.hit;
    }
}