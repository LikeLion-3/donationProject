package com.myapp.warmwave.domain.community.mapper;

import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

public interface CommunityUpdateMapper {
    Community communityPostDtoToCommunity(CommunityPostDto dto);
    CommunityResponseDto communityToCommunityResponseDto(Community community);
}
