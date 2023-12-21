package com.myapp.warmwave.domain.community.service;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.community.dto.CommunityPostDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.mapper.CommunityMapper;
import com.myapp.warmwave.domain.image.entity.Image;
import com.myapp.warmwave.domain.image.service.ImageService;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import com.myapp.warmwave.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityFacadeServiceTest {
    @Mock
    private CommunityService communityService;

    @Mock
    private ImageService imageService;

    @Mock
    private CommunityMapper communityMapper;

    @Mock
    private UserRepository<User> userRepository;

    @InjectMocks
    private CommunityFacadeService communityFacadeService;

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

    private Individual individual() {
        return Individual.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("a1234567")
                .nickname("닉네임")
                .profileImg(UserService.DEFAULT_PROFILE_IMG_INDI)
                .role(Role.INDIVIDUAL)
                .build();
    }

    @BeforeEach
    void setup() {
        image = Image.builder().id(1L).build();
    }

    @DisplayName("커뮤니티 글 생성 기능 확인")
    @Test
    void create() throws IOException {
        // given
        HttpServletRequest request = new MockHttpServletRequest();
        CommunityPostDto reqDto = CommunityPostDto.builder()
                        .title("제목").contents("내용").category("카테고리").build();

        when(communityMapper.communityPostDtoToCommunity(any())).thenReturn(community());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(individual()));
        when(communityService.saveCommunity(any())).thenReturn(community());
        when(imageService.uploadImagesForCommunity(any(), any()))
                .thenReturn(List.of(image));

        CommunityResponseDto resDto = CommunityResponseDto.builder()
                .id(1L).title("제목").contents("내용").userId(1L).category("카테고리")
                .writer("작성자").build();

        when(communityMapper.communityToCommunityResponseDto(any())).thenReturn(resDto);

        // when
        CommunityResponseDto createdDto = communityFacadeService.createCommunity(
                reqDto, List.of(new MockMultipartFile("이미지", "file".getBytes())),
                "test@gmail.com", request
        );

        // then
        assertThat(createdDto).isNotNull();
    }
}
