package com.myapp.warmwave.domain.email.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_EMAIL_AUTH")
@ToString
@EqualsAndHashCode
public class EmailAuth {

    private static final Long EMAIL_TOKEN_EXPIRE_TIME = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String authToken;
    private Boolean isVerified;
    private Boolean expired;    // 이메일 인증 토큰 만료여부
    private LocalDateTime expirationDate;   // 이메일 토큰 만료 시간

    @Builder
    public EmailAuth(Long id, String email, String authToken, Boolean expired, Boolean isVerified) {
        this.id = id;
        this.email = email;
        this.authToken = authToken;
        this.isVerified = isVerified;
        this.expired = expired;
        this.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRE_TIME);
    }

    public void usedToken() {
        this.expired = true;
    }

    public void emailVerified() {
        this.isVerified = true;
    }
}
