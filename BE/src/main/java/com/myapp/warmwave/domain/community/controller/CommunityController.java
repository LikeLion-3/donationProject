package com.myapp.warmwave.domain.community.controller;

import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityUpdateMapper;
import com.myapp.warmwave.domain.community.service.CommunityFacadeService;
import com.myapp.warmwave.domain.community.service.CommunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/communities")
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

    // 가보자구 ~!
    @GetMapping("/{community}")
    public ResponseEntity<List<CommunityResponseDto>> getCommunities(@PageableDefault(size=10) Pageable pageable) {

    }

    @PatchMapping("/{communityId}") // 수정 -> dto, 엔티티만. 이미지 X
    public ResponseEntity<CommunityResponseDto> updateCommunity(@PathVariable("communityId") Long communityId,
                                                                @RequestBody CommunityPatchDto dto) {
        Community community = communityFacadeService.updateCommunity(communityId, dto);
        return new ResponseEntity<>(communityMapper.communityToCommunityResponseDto(community), HttpStatus.OK);
    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity deleteMapping(@PathVariable("communityId") Long communityId) {
        communityService.deleteCommunity(communityId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/images/{communityId}") // 이미지 분리. 삭제는 추후
    public ResponseEntity<CommunityResponseDto> addCommunityImages(@PathVariable("communityId") Long communityId,
                                               @RequestPart List<MultipartFile> images) {
        Community community = communityFacadeService.addCommunityImages(communityId, images);
        return new ResponseEntity<>(communityMapper.communityToCommunityResponseDto(community), HttpStatus.OK);
    }
}
