package com.myapp.warmwave.domain.community.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.exception.CustomExceptionCode;
import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityMapper;
import com.myapp.warmwave.domain.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommunityRepository communityRepository;

    private final CommunityMapper communityMapper;

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
    public Community updateCommunity(Long communityId, CommunityPatchDto dto){
        Community originCommunity = getCommunity(communityId);
        return communityRepository.save(communityMapper.updateCommunity(originCommunity, dto));
    }

    @Transactional
    public void deleteCommunity(Long communityId) {
        communityRepository.delete(getCommunity(communityId));
    }
}
