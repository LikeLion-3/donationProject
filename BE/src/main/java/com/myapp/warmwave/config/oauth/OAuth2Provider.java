package com.myapp.warmwave.config.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
*   각 Provider 정보를 갖고 있는 클래스
*   getProvider() 를 통해 provider 정보를 가져온다.
 */
@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    KAKAO("kakao"), NAVER("naver"), GOOGLE("google");

    private final String provider;
}
