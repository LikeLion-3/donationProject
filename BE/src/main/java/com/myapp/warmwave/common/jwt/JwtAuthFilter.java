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
import java.util.HashMap;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = cookieManager.getCookie(request, ACCESS_TOKEN);
        String email = null;

        if (token == null) {
            System.out.println("token is not exists in cookies");
            return;
        }

        try {
            jwtProvider.isTokenExpired(token);
        } catch (CustomException e) {
            //토큰이 파기되었을 때 리프레시 토큰을 통해 액세스 토큰을 재발급
            if (e.getExceptionCode().equals(EXPIRED_JWT)) {
                Map<String, String> tokens = jwtRefreshService.refreshToken(request);
                cookieManager.setCookie(response, ACCESS_TOKEN, tokens.get(ACCESS_TOKEN), jwtProvider.getAccessTokenExpirationPeriod());
                cookieManager.setCookie(response, REFRESH_TOKEN, tokens.get(REFRESH_TOKEN), jwtProvider.getRefreshTokenExpirationPeriod());

                token = cookieManager.getCookie(request, ACCESS_TOKEN);
            }
        }

        Map<String, Object> claims = (HashMap<String, Object>) jwtProvider.getClaims(token).get("body");
        email = claims.get("email").toString();
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}