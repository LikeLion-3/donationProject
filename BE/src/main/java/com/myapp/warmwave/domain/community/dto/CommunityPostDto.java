package com.myapp.warmwave.domain.community.dto;

import com.myapp.warmwave.domain.community.entity.Community;
import lombok.Getter;

@Getter
public class CommunityPostDto {
    private String title;

    private String contents;

    private String category;
}
