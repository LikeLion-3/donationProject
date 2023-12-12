package com.myapp.warmwave.config.oauth.service;

import com.myapp.warmwave.config.oauth.OAuth2Attributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/*
*   CustomOAuth2UserService: OAuth2UserService 의 구현체
*   소셜 로그인을 시도한 유저의 요청을 End-User (Resource Owner) 에게 접근 토큰(Resource 보유하는 곳의 토큰)을 보내서
*   확인이 되면 인증된 OAuth2User 를 반환합니다.
*/

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    // OAuth2 요청을 통해 유저를 불러오는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); // service 내에서 OAuth2 정보를 가져온다

        // OAuth2 서비스 id (provider) -> 요청을 보낸 클라이언트의 소셜 사이트 ID를 가져옵니다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2User 에서 받은 정보를 provider 에 기반하여 재설정
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, oAuth2User);
        // 앞서 받은 정보를 Map 형태로 변환
        Map<String, Object> attributes = oAuth2Attributes.convertToMap();

        log.info(oAuth2Attributes.toString());

        // 최종적으로 OAuth2User 를 반환해준다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("INDIVIDUAL")),
                attributes,
                OAuth2Attributes.NAME_ATTRIBUTE_KEY
        );
    }
}
