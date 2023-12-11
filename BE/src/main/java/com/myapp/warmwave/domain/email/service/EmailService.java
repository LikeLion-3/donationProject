package com.myapp.warmwave.domain.email.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.domain.email.entity.EmailAuth;
import com.myapp.warmwave.domain.email.repository.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.INVALID_JWT;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {

    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender javaMailSender;

    @Transactional
    public EmailAuth createEmailAuth(String email) {
        return emailAuthRepository.save(EmailAuth.builder()
                .email(email)
                .authToken(UUID.randomUUID().toString())
                .isVerified(false)
                .expired(false)
                .build());
    }

    public EmailAuth validEmail(String email, String authToken, LocalDateTime datetime) {
        return emailAuthRepository.findValidAuthByEmail(email, authToken, datetime)
                .orElseThrow(() -> new CustomException(INVALID_JWT));
    }

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
