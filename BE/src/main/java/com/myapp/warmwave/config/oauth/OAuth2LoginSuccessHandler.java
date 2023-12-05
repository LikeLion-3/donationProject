package com.myapp.warmwave.config.oauth;

import com.myapp.warmwave.common.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
//@Transactional
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if (oAuth2User.getRole() == null) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("email", oAuth2User.getEmail());
                String accessToken = jwtProvider.createAccessToken(claims);
                response.addHeader(jwtProvider.getAccessHeader(), "Bearer " + accessToken);
                //TODO: 컨트롤러 작성 필요
//                response.sendRedirect("oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

                jwtProvider.sendAccessAndRefreshToken(response, accessToken, null);
            } else {
                loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", oAuth2User.getEmail());
        String accessToken = jwtProvider.createAccessToken(claims);
        String refreshToken = jwtProvider.createRefreshToken();
        response.addHeader(jwtProvider.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtProvider.getRefreshHeader(), "Bearer " + refreshToken);

        jwtProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    }
}
