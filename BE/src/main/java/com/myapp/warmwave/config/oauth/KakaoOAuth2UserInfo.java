package com.myapp.warmwave.config.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> email = (Map<String, Object>) account.get("email");

        if (account.isEmpty() || email.isEmpty()) {
            return null;
        }

        return (String) email.get("value");
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }
}