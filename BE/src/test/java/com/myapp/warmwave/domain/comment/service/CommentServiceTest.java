package com.myapp.warmwave.domain.comment.service;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.domain.comment.dto.CommentRequestDto;
import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import com.myapp.warmwave.domain.comment.entity.Comment;
import com.myapp.warmwave.domain.comment.mapper.CommentMapper;
import com.myapp.warmwave.domain.comment.repository.CommentRepository;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.repository.CommunityRepository;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private UserRepository<User> userRepository;

    @InjectMocks
    private CommentService commentService;

    private Individual individual;
    private Community community;
    private Comment comment;

    @BeforeEach
    void setup() {
        individual = Individual.builder()
                .id(1L).email("test@gmail.com").role(Role.INDIVIDUAL).build();

        community = Community.builder()
                .id(1L).title("제목").contents("내용").communityCategory(Community.CommunityCategory.etc)
                .comments(new ArrayList<>()).user(individual).build();

        comment = Comment.builder()
                .id(1L).user(individual).community(community)
                .contents("내용").build();
    }

    @DisplayName("댓글 작성 기능 확인")
    @Test
    void create() {
        // given
        Long communityId = 1L;
        String userEmail = "test@gmail.com";
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        CommentRequestDto reqDto = new CommentRequestDto("댓글 내용입니다. 글자수제한");

        when(communityRepository.findById(any())).thenReturn(Optional.of(community));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(individual));
        when(commentRepository.save(any())).thenReturn(comment);

        CommentResponseDto resDto = new CommentResponseDto(
                1L, "댓글 내용입니다. 글자수제한", LocalDateTime.now(), LocalDateTime.now(), "작성자", 1L, 1L
        );

        when(commentMapper.commentToCommentResponseDto(any())).thenReturn(resDto);

        // when
        CommentResponseDto result = commentService.createComment(reqDto, communityId, userEmail, httpServletRequest);

        // then
        assertThat(result.getContents()).isEqualTo(resDto.getContents());
    }

    @DisplayName("댓글 목록 조회 기능 확인")
    @Test
    void readAll() {
        // given
        Long communityId = 1L;
        String userEmail = "test@gmail.com";
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();
        saveComment(communityId, userEmail, httpServletRequest);

        String sort = "popular";
        Pageable pageable = PageRequest.of(0, 12);
        Page<Comment> pageList = new PageImpl<>(List.of(comment));
        CommentResponseDto resDto = new CommentResponseDto(
                1L, "댓글 내용입니다. 글자수제한", LocalDateTime.now(), LocalDateTime.now(), "작성자", 1L, 1L
        );
        Page<CommentResponseDto> dtoPage = new PageImpl<>(List.of(resDto));
        when(commentRepository.findComments(pageable, sort, communityId)).thenReturn(dtoPage);

        // when
        Page<CommentResponseDto> result = commentService.getComments(pageable, sort, communityId);

        // then
        assertThat(result).hasSize(1);
    }

    @DisplayName("댓글 수정 기능 확인")
    @Test
    void update() {
        // given
        Long communityId = 1L;
        String userEmail = "test@gmail.com";
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();

        CommentRequestDto reqDto = saveComment(communityId, userEmail, httpServletRequest);
        String originalContents = reqDto.getContents();

        Long commentId = 1L;

        comment.updateComment("댓글 내용 수정했습니다~!");
        when(commentRepository.save(any())).thenReturn(comment);

        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        // when
        CommentResponseDto resDto = commentService.updateComment(reqDto, commentId, communityId, userEmail);

        // then
        assertThat(resDto.getContents()).isEqualTo(originalContents);
    }

    @DisplayName("댓글 삭제 기능 확인")
    @Test
    void delete() {
        // given
        Long communityId = 1L;
        String userEmail = "test@gmail.com";
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();

        saveComment(communityId, userEmail, httpServletRequest);
        Long commentId = 1L;

        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        // when
        commentService.deleteComment(commentId, communityId);

        // then
        assertThat(commentRepository.count()).isZero();
    }

    private CommentRequestDto saveComment(Long communityId, String userEmail, HttpServletRequest httpServletRequest) {
        CommentRequestDto reqDto = new CommentRequestDto("댓글 내용입니다. 글자수제한");
        when(communityRepository.findById(any())).thenReturn(Optional.of(community));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(individual));
        when(commentRepository.save(any())).thenReturn(comment);

        CommentResponseDto resDto = new CommentResponseDto(
                1L, "댓글 내용입니다. 글자수제한", LocalDateTime.now(), LocalDateTime.now(), "작성자", 1L, 1L
        );

        when(commentMapper.commentToCommentResponseDto(any())).thenReturn(resDto);

        commentService.createComment(reqDto, communityId, userEmail, httpServletRequest);

        return reqDto;
    }
}
