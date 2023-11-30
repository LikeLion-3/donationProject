package com.myapp.warmwave.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserLoginDto {
    private Long id;
    private String accessToken;
    private String refreshToken;
}
