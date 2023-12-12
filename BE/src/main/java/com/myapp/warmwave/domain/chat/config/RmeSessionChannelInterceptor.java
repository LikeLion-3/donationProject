package com.myapp.warmwave.domain.chat.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Map;
@Component
public class RmeSessionChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("채널 인터셉터");

        MessageHeaders headers = message.getHeaders();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getMessageType() == SimpMessageType.CONNECT) {
            // 연결 설정 중에 헤더를 로깅
            MultiValueMap<String, String> multiValueMap = headers.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
            for (Map.Entry<String, String> header : multiValueMap.toSingleValueMap().entrySet()) {
                System.out.println(header.getKey() + "#" + header.getValue());
            }
        }

        return message;
    }
}