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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityResponseDto> createCommunity(@ModelAttribute CommunityPostDto dto,
                                                                List<MultipartFile> images,
                                                                @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        // + userIp 처리
        String userEmail = userDetails.getUsername();
        return new ResponseEntity<>(communityFacadeService.createCommunity(dto, images, userEmail), HttpStatus.CREATED);
    }

    @PutMapping("/{communityId}")
    public ResponseEntity<CommunityResponseDto> updateCommunity(@PathVariable("communityId") Long communityId,
                                                                @ModelAttribute CommunityPatchDto dto,
                                                                List<MultipartFile> images,
                                                                @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        System.out.println("image(controller) " + images); // null
        return new ResponseEntity<>(communityService.updateCommunity(communityId, dto, images), HttpStatus.OK);
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityResponseDto> getCommunity(@PathVariable("communityId") Long communityId) {
        return new ResponseEntity<>(communityFacadeService.getCommunity(communityId), HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<Page<CommunityListResponseDto>> getCommunities(@RequestParam(required = false, defaultValue = "recent") String sort,
                                                                        @PageableDefault(size=12) Pageable pageable) {
        return new ResponseEntity<>(communityService.getAllCommunities(pageable, sort), HttpStatus.OK);
    }

//    JPA
//    @GetMapping("")
//    public ResponseEntity<Slice<CommunityResponseDto>> getCommunities(@PageableDefault(size=10) Pageable pageable) {
//        return new ResponseEntity<>(communityService.findAll(pageable), HttpStatus.OK);
//    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity deleteMapping(@PathVariable("communityId") Long communityId) {
        communityFacadeService.deleteCommunity(communityId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
