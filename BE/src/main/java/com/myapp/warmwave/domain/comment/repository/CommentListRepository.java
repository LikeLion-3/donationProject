package com.myapp.warmwave.domain.comment.repository;

import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

public interface CommentListRepository {
    public Page<CommentResponseDto> findComments(Pageable pageable, String sort, Long communityId);
}
