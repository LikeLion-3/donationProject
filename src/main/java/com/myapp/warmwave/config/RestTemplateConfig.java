package com.myapp.warmwave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // HTTP 프로토콜을 이용해 외부 시스템과의 통신을 쉽게 할 수 있게 도와주는 클래스
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
