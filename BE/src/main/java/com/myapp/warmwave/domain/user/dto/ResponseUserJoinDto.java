package com.myapp.warmwave.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ResponseUserJoinDto {

    private Long id;
    private String email;
    private String authToken;

    public ResponseUserJoinDto(Long id, String email, String authToken) {
        this.id = id;
        this.email = email;
        this.authToken = authToken;
    }
}
