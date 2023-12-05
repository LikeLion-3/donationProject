package com.myapp.warmwave.domain.community.service;

import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityMapper;
import com.myapp.warmwave.domain.community.mapper.CommunityUpdateMapper;
import com.myapp.warmwave.domain.image.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CommunityFacadeService {
    private final CommunityService communityService;
    private final ImageService imageService;
    private final CommunityMapper communityMapper;

    public CommunityFacadeService(CommunityService communityService, ImageService imageService, CommunityMapper communityMapper) {
        this.communityService = communityService;
        this.imageService = imageService;
        this.communityMapper = communityMapper;
    }

    public Community createCommunity(CommunityPostDto dto, List<MultipartFile> images){
        Community createdCommunity = communityMapper.communityPostDtoToCommunity(dto);
        createdCommunity.setImages(imageService.uploadImagesForCommunity(createdCommunity, images));
        return createdCommunity;
    }

    public Community updateCommunity(Long communityId, CommunityPatchDto dto){
        Community originCommunity = communityService.getCommunity(communityId);
        return communityMapper.updateCommunity(originCommunity, dto); // null이 갔다
    }

    public Community addCommunityImages(Long communityId, List<MultipartFile> images) {
        Community community = communityService.getCommunity(communityId);
        imageService.uploadImagesForCommunity(community, images);
        return communityService.saveCommunity(community);
    }
}
