package com.myapp.warmwave.common.jwt;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.jwt.service.JwtRefreshService;
import com.myapp.warmwave.config.oauth.service.CustomUserDetailsService;
import com.myapp.warmwave.config.security.CookieManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.EXPIRED_JWT;
import static com.myapp.warmwave.config.security.CookieManager.ACCESS_TOKEN;
import static com.myapp.warmwave.config.security.CookieManager.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final CookieManager cookieManager;
    private final JwtRefreshService jwtRefreshService;

    // 필터 검증 예외 처리
    // shouldNotFilter()는 OncePerRequestFilter의 상위 클래스에 정의된 메서드, 필터로 체크하지 않을 경로나 메서드 등을 지정하기 위해서 사용
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        List<String> excludePatterns;

        // Preflight(Ajax 통신 실제 요청을 보내기 전에 OPTIONS 메서드를 사용하여 서버에 대한 정보를 요청) 요청은 체크하지 않음
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        if (request.getMethod().equals("GET")) {
            excludePatterns = List.of(
                    // 아티클 목록 조회, 단일 조회의 경우 필터에서 체크하지 않음
                    "/api/articles/**",
                    // 커뮤니티 목록 조회, 단일 조회의 경우 필터에서 체크하지 않음
                    "/api/communities/**",
                    // 이메일 링크 인증 경로의 호출은 체크하지 않음
                    "/api/users/confirm-email"
            );
            return excludePatterns.stream().anyMatch(path::startsWith);
        }

        if (request.getMethod().equals("POST")) {
            excludePatterns = List.of(
                    // 로그인 경로의 호출은 체크하지 않음.
                    "/api/users/login",
                    // 이메일중복체크 경로의 호출은 체크하지 않음.
                    "/api/users/register/checkEmail",
                    // 닉네임중복체크 경로의 호출은 체크하지 않음
                    "/api/users/register/checkNickname",
                    // 기관회원가입 경로의 호출은 체크하지 않음
                    "/api/users/register/institution",
                    // 개인회원가입 경로의 호출은 체크하지 않음
                    "/api/users/register/individual"
            );
            return excludePatterns.stream().anyMatch(path::startsWith);
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = cookieManager.getCookie(request, ACCESS_TOKEN);
        String email = null;

        if (token != null) {
            try {
                jwtProvider.isTokenExpired(token);
            } catch (CustomException e) {
                //토큰이 파기되었을 때 리프레시 토큰을 통해 액세스 토큰을 재발급
                if (e.getExceptionCode().equals(EXPIRED_JWT)) {
                    Map<String, String> tokens = jwtRefreshService.refreshToken(request);
                    cookieManager.deleteCookie(response, ACCESS_TOKEN);
                    cookieManager.deleteCookie(response, REFRESH_TOKEN);
                    cookieManager.setCookie(response, tokens.get(ACCESS_TOKEN), ACCESS_TOKEN, jwtProvider.getAccessTokenExpirationPeriod());
                    cookieManager.setCookie(response, tokens.get(REFRESH_TOKEN), REFRESH_TOKEN, jwtProvider.getRefreshTokenExpirationPeriod());

                    token = tokens.get(ACCESS_TOKEN);
                }
            }

            Map<String, Object> claims = (Map<String, Object>) jwtProvider.getClaims(token).get("body");
            email = claims.get("email").toString();
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}