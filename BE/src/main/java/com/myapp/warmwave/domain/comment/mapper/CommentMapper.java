package com.myapp.warmwave.domain.comment.mapper;


import com.myapp.warmwave.domain.comment.dto.CommentRequestDto;
import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import com.myapp.warmwave.domain.comment.entity.Comment;
import com.myapp.warmwave.domain.community.entity.Community;
import org.springframework.stereotype.Component;

@Component
public interface CommentMapper {
    public Comment commentPostDtoToComment(CommentRequestDto dto, Community community);

    public CommentResponseDto commentToCommentResponseDto(Comment comment);
}
