package com.myapp.warmwave.domain.chat.entity;

import com.myapp.warmwave.common.BaseEntity;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "TB_CHATROOM")
@EqualsAndHashCode
@ToString
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DONOR_ID")
    private Individual donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPIENT_ID")
    private Institution recipient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.PERSIST)
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    // 채팅 상태
    private String status;

    private LocalDateTime deletedAt;
}
