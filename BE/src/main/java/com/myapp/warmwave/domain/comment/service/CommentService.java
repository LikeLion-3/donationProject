package com.myapp.warmwave.domain.comment.service;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.exception.CustomExceptionCode;
import com.myapp.warmwave.domain.comment.dto.CommentRequestDto;
import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import com.myapp.warmwave.domain.comment.entity.Comment;
import com.myapp.warmwave.domain.comment.mapper.CommentMapper;
import com.myapp.warmwave.domain.comment.repository.CommentRepository;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.repository.CommunityRepository;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_FOUND_USER;
import static com.myapp.warmwave.common.exception.CustomExceptionCode.NOT_MATCH_WRITER;
import static com.myapp.warmwave.common.util.Utils.userIp.getUserIP;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommunityRepository communityRepository;
    private final UserRepository<User> userRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto, Long communityId, String userEmail, HttpServletRequest request) {

        Community community = validateCommunityId(communityId);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        Comment comment = Comment.builder()
                .contents(dto.getContents())
                .community(community)
                .user(user)
                .userIp(getUserIP(request))
                .build();

       Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentToCommentResponseDto(savedComment);
    }

    public Page<CommentResponseDto> getComments(Pageable pageable, String sort, Long communityId) {
        return commentRepository.findComments(pageable, sort, communityId);
    }

    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto dto, Long commentId, Long communityId, String userEmail) {
        Comment originComment = validateCommunityAndComment(commentId, communityId);

        if(!originComment.getUser().getEmail().equals(userEmail)) {
            throw new CustomException(NOT_MATCH_WRITER);
        }
        originComment.updateComment(dto.getContents());
        commentRepository.save(originComment);

        return commentMapper.commentToCommentResponseDto(originComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long communityId) {
        validateCommunityAndComment(commentId, communityId);
        commentRepository.deleteById(commentId);

        if(commentRepository.existsById(commentId))
            throw new CustomException(CustomExceptionCode.FAILED_TO_REMOVE);
    }

    private Community validateCommunityId(Long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE));
    }

    private Comment validateCommunityAndComment(Long commentId, Long communityId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_COMMENT));
        if (!comment.getCommunity().getId().equals(communityId))
            throw new CustomException(CustomExceptionCode.NOT_FOUND_ARTICLE);

        return comment;
    }

}
