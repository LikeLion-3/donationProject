package com.myapp.warmwave.domain.community.mapper;

import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.image.entity.Image;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class CommunityMapperImpl implements CommunityMapper{

    public Community communityPostDtoToCommunity(CommunityPostDto dto) {
        System.out.println(Community.CommunityCategory.fromTitle(dto.getCategory()));
        return Community.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .communityCategory(Community.CommunityCategory.fromTitle(dto.getCategory()))
                .build();
    }

    public CommunityResponseDto communityToCommunityResponseDto(Community community) {
        return CommunityResponseDto.builder()
                .id(community.getId())
                .writer(community.getUser().getName())
                .title(community.getTitle())
                .contents(community.getContents())
                .category(community.getCommunityCategory().getTitle())
                .hit(community.getHit())
                .images(community.getImages().stream()
                        .map(Image::getImgUrl)
                        .collect(Collectors.toList()))
                .createdAt(community.getCreatedAt())
                .build();
    }
    public Community updateCommunity(Community community, CommunityPatchDto dto) {
        if ( dto == null ) {
            return community;
        }

        if ( dto.getTitle() != null ) {
            community.setTitle( dto.getTitle() );
        }
        if ( dto.getContents() != null ) {
            community.setContents( dto.getContents() );
        }
        if ( dto.getCategory() != null ) {
            community.setCommunityCategory(Community.CommunityCategory.fromTitle(dto.getCategory()));
        }

        return community;
    }
}
