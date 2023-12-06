package com.myapp.warmwave.common.jwt.controller;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.exception.CustomExceptionCode;
import com.myapp.warmwave.common.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ApiRefreshController {

    private final JwtProvider jwtProvider;

    @GetMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(CustomExceptionCode.NULL_REFRESH);
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomException(CustomExceptionCode.UNSUPPORTED_TOKEN);
        }

        if (!jwtProvider.isTokenValid(refreshToken)) {
            throw new CustomException(CustomExceptionCode.INVALID_JWT);
        }

        String accessToken = authHeader.substring(7);

        //토큰이 만료되지 않고 유효한 경우
        if (jwtProvider.isTokenValid(accessToken)) {
            //원래 토큰 정보를 그대로 반환
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        Map<String, Object> claims = new HashMap<>();

        claims = jwtProvider.getClaims(refreshToken);

        String newAccessToken = jwtProvider.createAccessToken(claims);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) ? jwtProvider.createRefreshToken() : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    private boolean checkTime(Integer exp) {

        //JWT exp를 날짜로 변환
        java.util.Date expDate = new java.util.Date((long) exp * (1000));

        //현재 시간과의 차이 계산 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();

        //분단위 계산
        long leftMin = gap / (1000 * 60);

        //만료 시간이 얼마나 남았는지 체크하여 1시간 미만으로 남았을 경우 true를 반환
        return leftMin < 60;
    }
}
