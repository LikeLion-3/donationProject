package com.myapp.warmwave.jwt;

import com.myapp.warmwave.common.jwt.JwtProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtTests {
    @Autowired
    private JwtProvider jwtProvider;

    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;

    @Test
    @DisplayName("secretKey 키가 존재해야한다.")
    void t1() {
        assertThat(secretKeyPlain).isNotNull();
    }

    @Test
    @DisplayName("secretKey 원문으로 hmac 암호화 알고리즘에 맞는 SecretKey 객체를 만들 수 있다.")
    void t2() {
        // 키를 Base64 인코딩 한다.
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        // Base64 인코딩된 키를 이용하여 SecretKey 객체를 만든다.
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());

        assertThat(secretKey).isNotNull();
    }

    @Test
    @DisplayName("JwtProvider 객체로 SecretKey 객체를 생성할 수 있다.")
    void t3() {
        SecretKey secretKey = jwtProvider.getSecretKey();

        assertThat(secretKey).isNotNull();
    }

    @Test
    @DisplayName("SecretKey 객체는 단 한번만 생성되어야 한다.")
    void t4() {
        SecretKey secretKey1 = jwtProvider.getSecretKey();
        SecretKey secretKey2 = jwtProvider.getSecretKey();

        assertThat(secretKey1 == secretKey2).isSameAs(true);
    }


    @Test
    @DisplayName("accessToken 을 얻는다.")
    void t5() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "admin@email.com");

        // 지금으로부터 5시간의 유효기간을 가지는 토큰을 생성
        String accessToken = jwtProvider.createAccessToken(claims);

        System.out.println("accessToken : " + accessToken);

        assertThat(accessToken).isNotNull();
    }


    @Test
    @DisplayName("accessToken 을 통해서 claims 를 얻을 수 있다.")
    void t6() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "admin@email.com");

        String accessToken = jwtProvider.createAccessToken(claims);

        System.out.println("accessToken : " + accessToken);

        assertThat(jwtProvider.isTokenValid(accessToken)).isTrue();

        Map<String, Object> claimsFromToken = (Map<String, Object>) jwtProvider.getClaims(accessToken).get("body");
        System.out.println("claimsFromToken : " + claimsFromToken);

        assertThat(claimsFromToken).containsEntry("email", "admin@email.com");
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증")
    void t7() {
        Map<String, Object> claims = new HashMap<>();
        String accessToken = "";

        assertThat(jwtProvider.isTokenValid(accessToken)).isFalse();
    }
}
