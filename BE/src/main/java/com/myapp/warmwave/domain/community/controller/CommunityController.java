package com.myapp.warmwave.domain.community.controller;

import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityMapper;
import com.myapp.warmwave.domain.community.service.CommunityFacadeService;
import com.myapp.warmwave.domain.community.service.CommunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityFacadeService communityFacadeService;
    private final CommunityService communityService;
    private final CommunityMapper communityMapper;

    public CommunityController(CommunityFacadeService communityFacadeService, CommunityService communityService, CommunityMapper communityMapper) {
        this.communityFacadeService = communityFacadeService;
        this.communityService = communityService;
        this.communityMapper = communityMapper;
    }

    @PostMapping("")
    public ResponseEntity<CommunityResponseDto> createCommunity(@RequestPart List<MultipartFile> images,
                                                                @RequestPart CommunityPostDto communityPostDto,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        // + userIp 처리
        String userEmail = userDetails.getUsername();
        return new ResponseEntity<>(communityFacadeService.createCommunity(communityPostDto, images, userEmail), HttpStatus.CREATED);
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityResponseDto> getCommunity(@PathVariable("communityId") Long communityId) {
        return new ResponseEntity<>(communityFacadeService.getCommunity(communityId), HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<Slice<CommunityListResponseDto>> getCommunities(@PageableDefault(size=10) Pageable pageable) {
        return new ResponseEntity<>(communityService.getAllCommunities(pageable), HttpStatus.OK);
    }

//    JPA
//    @GetMapping("")
//    public ResponseEntity<Slice<CommunityResponseDto>> getCommunities(@PageableDefault(size=10) Pageable pageable) {
//        return new ResponseEntity<>(communityService.findAll(pageable), HttpStatus.OK);
//    }

    @PatchMapping("/{communityId}") // 수정 : 이미지 수정 분리
    public ResponseEntity<CommunityResponseDto> updateCommunity(@PathVariable("communityId") Long communityId,
                                                                @RequestBody CommunityPatchDto dto) {
        Community community = communityService.updateCommunity(communityId, dto);
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
        return new ResponseEntity<>(communityFacadeService.addCommunityImages(communityId, images), HttpStatus.OK);
    }
}
