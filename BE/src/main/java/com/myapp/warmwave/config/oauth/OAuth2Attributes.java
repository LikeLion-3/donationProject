package com.myapp.warmwave.config.oauth;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

import static com.myapp.warmwave.config.oauth.OAuth2Provider.KAKAO;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
 */
@Slf4j
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Attributes {
    public static final String NAME_ATTRIBUTE_KEY = "email"; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미

    // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)
    private String id;
    private String name;
    private String email;
    private String provider;
    private String profileImg;

    @Builder
    public OAuth2Attributes(String id, String name, String email, String provider, String profileImg) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.profileImg = profileImg;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuth2Attributes of(String provider, OAuth2User oAuth2User) {
        if (provider.equals(KAKAO.getProvider())) {
            return ofKakao(oAuth2User);
        }

        return null;
    }

    private static OAuth2Attributes ofKakao(OAuth2User oAuth2User) {
        Map<String, Object> account = oAuth2User.getAttribute("kakao_account");
        log.info(account.toString());
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return new OAuth2Attributes(
                oAuth2User.getAttribute("id").toString(),
                (String) profile.get("nickname"),
                (String) account.get("email"),
                KAKAO.getProvider(),
                (String) profile.get("profile_image_url")
        );
    }

    // 추후 네이버, 구글 등 추가함.

    // OAuth2Attributes 의 정보를 기반으로 Map 으로 변환하는 메서드
    public Map<String, Object> convertToMap() {
        Map<String, Object> changedAttributes = new HashMap<>();
        changedAttributes.put("id", this.id);
        changedAttributes.put("name", this.name);
        changedAttributes.put("email", this.email);
        changedAttributes.put("provider", this.provider);
        changedAttributes.put("profile_img", this.profileImg);

        return changedAttributes;
    }
}
