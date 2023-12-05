package com.myapp.warmwave.domain.community.mapper;

import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import org.springframework.stereotype.Component;

@Component
public class CommunityUpdateMapperImpl {
    public Community communityPostDtoToCommunity(CommunityPostDto dto) {
        return Community.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .category(dto.getContents())
                .build();
    }

    public CommunityResponseDto communityToCommunityResponseDto(Community community) {
        return CommunityResponseDto.builder()
                .title(community.getTitle())
                .contents(community.getContents())
                .category(community.getCategory())
                .hit(community.getHit())
                .createdAt(community.getCreatedAt())
                .build();
    }
}
