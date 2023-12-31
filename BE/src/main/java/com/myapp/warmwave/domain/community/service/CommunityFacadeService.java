package com.myapp.warmwave.domain.community.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityMapper;
import com.myapp.warmwave.domain.image.service.ImageService;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_USER;
import static com.myapp.warmwave.common.util.Utils.userIp.getUserIP;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityFacadeService {
    private final CommunityService communityService;
    private final ImageService imageService;
    private final CommunityMapper communityMapper;
    private final UserRepository<User> userRepository;

    @Transactional
    public CommunityResponseDto createCommunity(CommunityPostDto dto, List<MultipartFile> images, String userEmail, HttpServletRequest request) throws IOException {
        // refactor : 파라미터 dto -> 매핑 로직이 facade 따위에 의존함 + setter
        Community community = communityMapper.communityPostDtoToCommunity(dto);
        community.setUser(userRepository.findByEmail(userEmail).orElseThrow(() -> new CustomException(NOT_FOUND_USER)));
        community.setUserIp(getUserIP(request));

        Community createdCommunity = communityService.saveCommunity(community);
        createdCommunity.setImages(imageService.uploadImagesForCommunity(createdCommunity, images));
        return communityMapper.communityToCommunityResponseDto(createdCommunity);
    }

//    public CommunityResponseDto getCommunity(Long communityId) {
//        return communityMapper.communityToCommunityResponseDto(communityService.getCommunity(communityId));
//    }

    @Transactional
    public void deleteCommunity(Long communityId) {
        imageService.deleteImagesByCommunityId(communityId);
        communityService.deleteCommunity(communityId);
    }
}
