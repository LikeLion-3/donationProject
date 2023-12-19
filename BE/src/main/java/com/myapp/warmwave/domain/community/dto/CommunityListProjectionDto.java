package com.myapp.warmwave.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommunityListProjectionDto {
    private Long id;

    private String title;

    private Integer hit;

    private Long userId;

    private String category;

    private LocalDateTime createdAt;
}
