package com.myapp.warmwave.config.oauth;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.common.jwt.JwtProvider;
import com.myapp.warmwave.config.oauth.service.CustomUserDetailsService;
import com.myapp.warmwave.domain.email.entity.EmailAuth;
import com.myapp.warmwave.domain.email.service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
*   OAuth2가 성공했을 때, 실행되는 핸들러
*   여기서 OAuth 정보를 기반으로 객체를 최종 생성한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        log.info("객체 생성 시작");
        String email = oAuth2User.getAttribute("email");
        String username = email.split("@")[0];
        String provider = oAuth2User.getAttribute("provider");
        String providerId = oAuth2User.getAttribute("id").toString();
        String profileImg = oAuth2User.getAttribute("profile_img");

        EmailAuth emailAuth = emailService.createEmailAuthOfSocial(email);

        if (!userDetailsService.userExists(email)) {
            userDetailsService.createUser(new CustomUserDetails(
                            username, email, providerId,
                            Role.INDIVIDUAL, profileImg, provider,
                            providerId, 0F, emailAuth
                    )
            );
        }
        log.info("객체 생성 완료 - {}", userDetailsService.loadUserByUsername(email).toString());

        log.info("토큰 생성 시작");

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        String accessToken = jwtProvider.createAccessToken(claims);
        String refreshToken = jwtProvider.createRefreshToken();
        response.addHeader(jwtProvider.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtProvider.getRefreshHeader(), "Bearer " + refreshToken);

        log.info("토큰 생성 완료 -> 헤더에 주입 완료");

        clearAuthenticationAttributes(request);
    }
}
