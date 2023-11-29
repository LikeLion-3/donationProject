package com.myapp.warmwave.common.security.service;

import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        OAuth2User oAuth2User = super.loadUser(userRequest);

        //현재는 카카오 소셜 로그인만 구현되어 있지만, 추후에 구글이나 네이버 등과 같은 소셜 로그인이 구현되면
        //구분자(providerTypeCode)에 따라 사용자의 닉네임이 설정될 수 있도록 구현해주어야 한다.
        String oauthId = switch (providerTypeCode) {
            //KAKAO 하드 코딩
            case "KAKAO" -> oAuth2User.getAttribute("id").toString();
            default -> oAuth2User.getName();
        };

        String userId = providerTypeCode + "__%s".formatted(oauthId);

        User user = userService.whenSocialLogin(oAuth2User, userId, providerTypeCode);

        return new CustomOAuth2User(user.getEmail(), user.getPassword(), user.getGrantedAuthorities());
    }
}

class CustomOAuth2User extends org.springframework.security.core.userdetails.User implements OAuth2User {
    public CustomOAuth2User(String userId, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userId, password, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
