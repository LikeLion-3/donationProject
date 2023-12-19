package com.myapp.warmwave.domain.community.dto;

import com.myapp.warmwave.domain.image.entity.Image;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityResponseDto {
    private Long id;

    private String title;

    private String contents;

    private Integer hit;

    private String writer;

    private String category;

    private List<String> images;

    private LocalDateTime createdAt;

    private Long userId;
}
