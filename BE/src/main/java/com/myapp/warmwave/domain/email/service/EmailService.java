package com.myapp.warmwave.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void send(String email, String authToken) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("WarmWave 회원가입 이메일 인증");

            String text = "WarmWave 가입해 주셔서 감사합니다. 아래 링크를 클릭해서 이메일 주소를 인증하세요\n\n";
            text += "http://localhost:8080/api/users/confirm-email?email=" + email + "&authToken=" + authToken;
            message.setText(text);

            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
