package com.myapp.warmwave.domain.community.mapper;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import com.myapp.warmwave.domain.user.service.UserService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class CommunityMapperImpl implements CommunityMapper{

    public Community communityPostDtoToCommunity(CommunityPostDto dto) {
        return Community.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .category(dto.getCategory())
                .build();
    }

    public CommunityResponseDto communityToCommunityResponseDto(Community community) {
        return CommunityResponseDto.builder()
                .id(community.getId())
                .writer(community.getUser().getName()) // getUser -> post할 땐 NPE, get할 땐 잘 나옴 .. // lazy
                .title(community.getTitle())
                .contents(community.getContents())
                .category(community.getCategory())
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
            community.setCategory( dto.getCategory() );
        }

        return community;
    }

}
