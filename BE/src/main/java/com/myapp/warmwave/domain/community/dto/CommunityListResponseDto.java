package com.myapp.warmwave.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CommunityListResponseDto {
    private Long id;

    private String title;

    private Integer hit;

    private String writer;

    private String category;

    private String imgUrl;

    private LocalDateTime createdAt;
}
