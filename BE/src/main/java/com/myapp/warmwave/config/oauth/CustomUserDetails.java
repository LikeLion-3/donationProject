package com.myapp.warmwave.config.oauth;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.email.entity.EmailAuth;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/*
*   UserDetails 를 구현한 구현체
*   실제 Entity 와 유사하게 설정한다.
*
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private String profileImg;
    private String provider;
    private String providerId;
    private Float temperature;
    private EmailAuth emailAuth;

    @Builder
    public CustomUserDetails(
            String username, String email, String password,
            Role role, String profileImg, String provider,
            String providerId, Float temperature, EmailAuth emailAuth
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.profileImg = profileImg;
        this.provider = provider;
        this.providerId = providerId;
        this.temperature = temperature;
        this.emailAuth = emailAuth;
    }

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public CustomUserDetails(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    public static CustomUserDetails of(User user) {
        return new CustomUserDetails(user);
    }

    public static CustomUserDetails of(String email, Role role) {
        return new CustomUserDetails(email, role);
    }

    // CustomUserDetails 의 정보를 통해 엔티티를 생성한다.
    public User newEntity() {
        return Individual.builder()
                .email(email)
                .nickname(username)
                .role(role)
                .profileImg(profileImg)
                .password(password)
                .provider(provider)
                .providerId(providerId)
                .temperature(temperature)
                .emailAuth(emailAuth)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("INDIVIDUAL"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
