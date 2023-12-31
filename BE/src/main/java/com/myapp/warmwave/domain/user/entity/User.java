package com.myapp.warmwave.domain.user.entity;

import com.myapp.warmwave.common.BaseEntity;
import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.address.entity.Address;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.chat.entity.ChatMessage;
import com.myapp.warmwave.domain.email.entity.EmailAuth;
import com.myapp.warmwave.domain.favorite_inst.entity.FavoriteInst;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Table(name = "TB_USER")
public abstract class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImg;

    private Float temperature;

    // OAuth2 용 컬럼 (소셜 로그인 타입, 개인 고유 등록 ID)
    private String provider;
    private String providerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMAILAUTH_ID")
    private EmailAuth emailAuth;  // 이메일 인증 여부(회원가입 후 진행)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ChatMessage> messageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "individualUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<FavoriteInst> favoriteList = new ArrayList<>();

    abstract public String getName();

    public void updateUserInfo(String password, Address address) {
        this.password = password;
        this.address = address;
//        this.profileImg = profileImg;
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (this.role == Role.INSTITUTION) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.INSTITUTION.getRole()));
        }

        if (this.role == Role.INDIVIDUAL) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.INDIVIDUAL.getRole()));
        }

        if (Role.ADMIN.equals(this.role)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.ADMIN.getRole()));
        }

        return grantedAuthorities;
    }
}
