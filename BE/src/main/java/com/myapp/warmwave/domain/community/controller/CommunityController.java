package com.myapp.warmwave.domain.community.controller;

import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityUpdateMapper;
import com.myapp.warmwave.domain.community.service.CommunityFacadeService;
import com.myapp.warmwave.domain.community.service.CommunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityFacadeService communityFacadeService;
    private final CommunityService communityService;
    private final CommunityUpdateMapper communityMapper;

    public CommunityController(CommunityFacadeService communityFacadeService, CommunityService communityService, CommunityUpdateMapper communityMapper) {
        this.communityFacadeService = communityFacadeService;
        this.communityService = communityService;
        this.communityMapper = communityMapper;
    }


    @PostMapping("")
    public ResponseEntity<CommunityResponseDto> createCommunity(@RequestPart List<MultipartFile> images,
                                                                @RequestPart CommunityPostDto communityPostDto) {
        // userIp 처리
        Community community = communityFacadeService.createCommunity(communityPostDto, images);
        return new ResponseEntity<>(communityMapper.communityToCommunityResponseDto(community), HttpStatus.CREATED);
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityResponseDto> getCommunity(@PathVariable("communityId") Long communityId) {
        Community community = communityService.getCommunity(communityId);
        return new ResponseEntity<>(communityMapper.communityToCommunityResponseDto(community), HttpStatus.OK);
    }

//    목록 pageable 구현
//    @GetMapping

    @PatchMapping("/{communityId}") // 수정 -> dto, 엔티티만. 이미지 X
    public ResponseEntity<CommunityResponseDto> updateCommunity(@PathVariable("communityId") Long communityId,
                                                                @RequestBody CommunityPatchDto dto) {
        Community community = communityFacadeService.updateCommunity(communityId, dto);
        return new ResponseEntity<>(communityMapper.communityToCommunityResponseDto(community), HttpStatus.OK);
    }

    @PostMapping("/images/{communityId}") // 이미지 분리
    public ResponseEntity<CommunityResponseDto> addCommunityImages(@PathVariable("communityId") Long communityId,
                                               @RequestPart List<MultipartFile> images) {
        Community community = communityFacadeService.addCommunityImages(communityId, images);
        return new ResponseEntity<>(communityMapper.communityToCommunityResponseDto(community), HttpStatus.OK);
    }

}
