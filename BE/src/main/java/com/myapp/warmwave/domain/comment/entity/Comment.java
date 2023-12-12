package com.myapp.warmwave.domain.comment.entity;

import com.myapp.warmwave.common.BaseEntity;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="tb_comment")
@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String contents;

    @Column(name = "user_ip")
    private String userIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    public void updateComment(String contents) {
        this.contents = contents;
    }
}
