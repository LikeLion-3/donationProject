package com.myapp.warmwave.domain.community.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.exception.CustomExceptionCode;
import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityMapper;
import com.myapp.warmwave.domain.community.repository.CommunityRepository;
import com.myapp.warmwave.domain.image.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_ARTICLE;
import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_MATCH_WRITER;
import static com.myapp.warmwave.common.util.Utils.userIp.getUserIP;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final ImageService imageService;


    @Transactional
    public Community saveCommunity(Community community) {
        return communityRepository.save(community);
    }

    @Transactional
    public CommunityResponseDto getCommunity(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));
        community.setHit(community.getHit()+1);
        communityRepository.save(community);
        return communityMapper.communityToCommunityResponseDto(community);
    }

    public Page<CommunityListResponseDto> getAllCommunities(Pageable pageable, String sort) {
        return communityRepository.findAllCommunities(pageable, sort);
    }

    @Transactional
    public CommunityResponseDto updateCommunity(Long communityId, CommunityPatchDto dto, List<MultipartFile> images, String userEmail, HttpServletRequest request) throws IOException {
        System.out.println("images(service) : " + images); // null

        Community originCommunity = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ARTICLE));

        if(!originCommunity.getUser().getEmail().equals(userEmail)) {
            throw new CustomException(NOT_MATCH_WRITER);
        }

        // 사진 지우고, 새로 등록하고, dto 데이터들로 업데이트 하고 세이브
        if(!CollectionUtils.isEmpty(images)) {
            imageService.deleteImagesByCommunityId(communityId); // s3에서만 삭제
            originCommunity.getImages().clear(); // 이미지(DB) 삭제
        }
        communityMapper.updateCommunity(originCommunity, dto);
        originCommunity.getImages().addAll(imageService.uploadImagesForCommunity(originCommunity, images));
        originCommunity.setUserIp(getUserIP(request));

        Community updatedCommunity = saveCommunity(originCommunity);
        return communityMapper.communityToCommunityResponseDto(updatedCommunity);
    }

    @Transactional
    public void deleteCommunity(Long communityId) {
        communityRepository.deleteById(communityId);

        if(communityRepository.existsById(communityId))
            throw new CustomException(CustomExceptionCode.FAILED_TO_REMOVE);
    }
}
