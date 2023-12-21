package com.myapp.warmwave.domain.comment.mapper;

import com.myapp.warmwave.domain.comment.dto.CommentRequestDto;
import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import com.myapp.warmwave.domain.comment.entity.Comment;
import com.myapp.warmwave.domain.community.entity.Community;
import org.springframework.stereotype.Component;

@Component
public class CommentMapperImpl implements CommentMapper{
    @Override
    public Comment commentPostDtoToComment(CommentRequestDto dto, Community community) {
        return Comment.builder()
                .contents(dto.getContents())
                .community(community)
                .build();
    }

    @Override
    public CommentResponseDto commentToCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .writer(comment.getUser().getName())
                .communityId(comment.getCommunity().getId())
                .build();
    }
}
