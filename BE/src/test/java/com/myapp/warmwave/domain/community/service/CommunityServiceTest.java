package com.myapp.warmwave.domain.community.service;

import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityMapper;
import com.myapp.warmwave.domain.community.repository.CommunityRepository;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.image.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {
    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private CommunityMapper communityMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private CommunityService communityService;

    private Image image;

    private Community community() {
        return Community.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .communityCategory(Community.CommunityCategory.notice)
                .comments(new ArrayList<>())
                .images(new ArrayList<>())
                .hit(0)
                .build();
    }

    @BeforeEach
    void setup() {
        image = Image.builder().id(1L).build();
    }

    @Test
    @DisplayName("커뮤니티 글 생성 확인")
    void createCommunity() {
        // given
        Community community = community();
        when(communityRepository.save(any())).thenReturn(community);

        // when
        Community savedCommunity = communityService.saveCommunity(community);

        // then
        assertThat(savedCommunity).isNotNull();
    }

    @Test
    @DisplayName("커뮤니티 글 목록 조회 확인")
    void readAll() {
        // given
        Community community = community();
        when(communityRepository.save(any())).thenReturn(community);
        Community savedCommunity = communityService.saveCommunity(community);

        CommunityListResponseDto resDto = new CommunityListResponseDto(
                1L, "제목", 0, "작성자", "공지사항", LocalDateTime.now()
        );

        Pageable pageable = PageRequest.of(1, 5);

        Page<CommunityListResponseDto> dtoPage = new PageImpl<>(List.of(resDto));
        when(communityRepository.findAllCommunities(any(), any())).thenReturn(dtoPage);

        // when
        Page<CommunityListResponseDto> foundCommunity = communityService.getAllCommunities(pageable, "정렬조건");

        // then
        assertThat(foundCommunity).hasSize(1);
    }
}
