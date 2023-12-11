package com.myapp.warmwave.domain.community.mapper;


import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import org.springframework.stereotype.Component;

@Component
public interface CommunityMapper {
    Community communityPostDtoToCommunity(CommunityPostDto dto);

    CommunityResponseDto communityToCommunityResponseDto(Community community);

    Community updateCommunity(Community community, CommunityPatchDto dto);
}
