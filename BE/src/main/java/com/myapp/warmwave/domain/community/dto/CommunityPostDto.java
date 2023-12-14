package com.myapp.warmwave.domain.community.dto;

import lombok.*;

@Getter
@Builder
public class CommunityPostDto {
    private String title;

    private String contents;

    private String category;
}
