package com.myapp.warmwave.domain.chat.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 스프링 시큐리티 컨텍스트에서 사용자 정보 추출 및 WebSocket 속성으로 전달
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        attributes.put("username", authentication.getName());
        return true;
    }
}
