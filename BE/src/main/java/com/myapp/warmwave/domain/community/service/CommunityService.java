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
import java.util.stream.Collectors;

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

    public Community getCommunity(Long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));
    }

    public Page<CommunityListResponseDto> getAllCommunities(Pageable pageable, String sort) {
        return communityRepository.findAllCommunities(pageable, sort);
    }

//    JPA
//    @Transactional
//    public Page<Community> getAllCommunities(Pageable pageable) {
//        return communityRepository.findAll(pageable);
//    }

    @Transactional
    public CommunityResponseDto updateCommunity(Long communityId, CommunityPatchDto dto, List<MultipartFile> images, String userEmail, HttpServletRequest request) throws IOException {
        System.out.println("images(service) : " + images); // null

        Community originCommunity = getCommunity(communityId);

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
        communityRepository.delete(getCommunity(communityId));

        if(communityRepository.existsById(communityId))
            throw new CustomException(CustomExceptionCode.FAILED_TO_REMOVE);
    }

    private List<String> filterImageUrls(List<String> originUrls, List<String> updatedUrls) {
        // 원래 이미지 url 배열에서 -> dto 이미지 url 배열과 일치하지 않는 것들을 필터링
        return originUrls.stream()
                .filter(originUrl -> updatedUrls.stream().noneMatch(updatedUrl -> updatedUrl.equals(originUrl)))
                .collect(Collectors.toList());
    }

}
