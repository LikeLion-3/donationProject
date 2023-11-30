package com.myapp.warmwave.domain.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestEmailAuthDto {
    String email;
    String authToken;
}
