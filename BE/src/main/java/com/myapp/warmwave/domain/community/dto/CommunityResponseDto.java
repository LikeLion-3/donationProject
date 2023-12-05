package com.myapp.warmwave.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CommunityResponseDto {
    // 작성자, 이미지 추가해야 함
    private Long id;

    private String title;

    private String contents;

    private String category;

    private Integer hit;

    private LocalDateTime createdAt;
}
