package com.myapp.warmwave.domain.community.service;

import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.dto.CommunityPatchDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityMapper;
import com.myapp.warmwave.domain.community.repository.CommunityRepository;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.image.service.ImageService;
import com.myapp.warmwave.domain.user.entity.Individual;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                .user(Individual.builder().id(1L).email("test@gmail.com").build())
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
    @DisplayName("커뮤니티 글 생성 기능 확인")
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
    @DisplayName("커뮤니티 글 목록 조회 기능 확인")
    void readAll() {
        // given
        Community community = community();
        when(communityRepository.save(any())).thenReturn(community);
        communityService.saveCommunity(community);

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

    @DisplayName("커뮤니티 글 단일 조회 기능 확인")
    @Test
    void read() {
        // given
        Community community = community();
        when(communityRepository.save(any())).thenReturn(community);
        Community savedCommunity = communityService.saveCommunity(community);

        Long communityId = 1L;

        CommunityResponseDto resDto = new CommunityResponseDto(
              1L, "제목", "내용", 1, "작성자", "카테고리", null, null, null
        );

        when(communityRepository.findById(any())).thenReturn(Optional.of(savedCommunity));
        when(communityMapper.communityToCommunityResponseDto(any())).thenReturn(resDto);

        // when
        CommunityResponseDto foundCommunity = communityService.getCommunity(communityId);

        // then
        assertThat(foundCommunity).isNotNull();
    }

    @DisplayName("커뮤니티 글 정보 수정 기능 확인")
    @Test
    void update() throws IOException {
        // given
        HttpServletRequest req = new MockHttpServletRequest();
        Community community = community();
        String contents = "내용";
        when(communityRepository.save(any())).thenReturn(community);
        communityService.saveCommunity(community);

        when(communityRepository.findById(any())).thenReturn(Optional.of(community));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("test@gmail.com", "1234");
        SecurityContextHolder.getContext().setAuthentication(token);

        Long communityId = 1L;

        CommunityPatchDto dto = CommunityPatchDto.builder()
                .title("제목 수정")
                .contents("내용 수정")
                .category(Community.CommunityCategory.etc.getTitle())
                .build();

        Community updatedCommunity = Community.builder()
                .id(1L)
                .title(dto.getTitle())
                .contents(dto.getContents())
                .communityCategory(Community.CommunityCategory.etc)
                .build();

        List<MultipartFile> imageFiles = List.of(new MockMultipartFile("이미지", "file".getBytes()));
        List<Image> images = List.of(Image.builder().id(1L).build());

        when(communityMapper.updateCommunity(any(), any())).thenReturn(updatedCommunity);
        when(imageService.uploadImagesForCommunity(any(), any())).thenReturn(images);

        when(communityRepository.save(any())).thenReturn(updatedCommunity);
        communityService.saveCommunity(community);

        CommunityResponseDto resDto = CommunityResponseDto.builder()
                .id(1L)
                .title(updatedCommunity.getTitle())
                .contents(updatedCommunity.getContents())
                .category(updatedCommunity.getCommunityCategory().getTitle())
                .build();

        when(communityMapper.communityToCommunityResponseDto(any())).thenReturn(resDto);

        // when
        communityService.updateCommunity(communityId, dto, imageFiles, "test@gmail.com", req);

        // then
        assertThat(resDto.getContents()).isNotEqualTo(contents);
    }

    @DisplayName("커뮤니티 글 삭제 기능 확인")
    @Test
    void delete() {
        // given
        Community community = community();
        when(communityRepository.save(any())).thenReturn(community);
        communityService.saveCommunity(community);

        Long communityId = 1L;

        // when
        communityService.deleteCommunity(communityId);

        // then
        assertThat(communityRepository.count()).isZero();
    }
}
