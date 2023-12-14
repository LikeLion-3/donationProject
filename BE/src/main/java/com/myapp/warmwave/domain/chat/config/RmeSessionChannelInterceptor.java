package com.myapp.warmwave.domain.chat.config;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.exception.CustomExceptionCode;
import com.myapp.warmwave.common.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RmeSessionChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private WebsocketUserContext websocketUserContext;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // Access headers for all frames
        Map<String, List<String>> nativeHeaders = accessor.getMessageHeaders().get("nativeHeaders", Map.class);

        if (nativeHeaders != null) {
            List<String> userHeaderValue = nativeHeaders.get("Authorization");
            if (userHeaderValue != null && !userHeaderValue.isEmpty()) {
                String userHeader = userHeaderValue.get(0);
                //사용자 정보 처리
                String accessToken = userHeader.replace("Bearer ", "");
                Map<String, Object> claims = (HashMap<String, Object>) jwtProvider.getClaims(accessToken).get("body");
                String email = claims.get("email").toString();
                websocketUserContext.setUserEmail(email);
            } else {
                // 사용자 정보를 찾을 수 없는 경우 처리
                throw new CustomException(CustomExceptionCode.ILLEGAL_ARGUMENT_JWT);
            }
        }

        return message;
    }
}