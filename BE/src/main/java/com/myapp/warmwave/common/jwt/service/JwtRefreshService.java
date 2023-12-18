package com.myapp.warmwave.common.jwt.service;

import com.myapp.warmwave.common.jwt.JwtProvider;
import com.myapp.warmwave.config.security.CookieManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.myapp.warmwave.config.security.CookieManager.ACCESS_TOKEN;
import static com.myapp.warmwave.config.security.CookieManager.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class JwtRefreshService {
    private final JwtProvider jwtProvider;
    private final CookieManager cookieManager;

    public Map<String, String> refreshToken(HttpServletRequest request) {
        String accessToken = cookieManager.getCookie(request, ACCESS_TOKEN);
        String refreshToken = cookieManager.getCookie(request, REFRESH_TOKEN);

        //토큰이 만료되지 않고 유효한 경우
        if (jwtProvider.isTokenValid(accessToken)) {
            //원래 토큰 정보를 그대로 반환
            return Map.of(ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken);
        }

        //리프레시 토큰이 만료되었어도 클레임을 가져옴
        Map<String, Object> refreshTokenClaims = jwtProvider.getClaims(refreshToken);
        Map<String, Object> accessTokenClaims = (Map<String, Object>) jwtProvider.getClaims(accessToken).get("body");

        String newAccessToken = jwtProvider.createAccessToken(accessTokenClaims);

        String newRefreshToken = checkTime((Long) refreshTokenClaims.get("exp")) ? jwtProvider.createRefreshToken() : refreshToken;

        return Map.of(ACCESS_TOKEN, newAccessToken, REFRESH_TOKEN, newRefreshToken);
    }
    private boolean checkTime(Long exp) {

        //JWT exp를 날짜로 변환
        java.util.Date expDate = new java.util.Date(exp * (1000));

        //현재 시간과의 차이 계산 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();

        //분단위 계산
        long leftMin = gap / (1000 * 60);

        //만료 시간이 얼마나 남았는지 체크하여 1시간 미만으로 남았을 경우 true를 반환
        return leftMin < 60;
    }
}
