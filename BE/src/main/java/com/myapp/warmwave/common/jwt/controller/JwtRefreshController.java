package com.myapp.warmwave.common.jwt.controller;

import com.myapp.warmwave.common.jwt.service.JwtRefreshService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class JwtRefreshController {
    private final JwtRefreshService jwtRefreshService;

    @GetMapping("/refresh")
    public Map<String, String> refresh(HttpServletRequest request) {
        return jwtRefreshService.refreshToken(request);
    }
}
