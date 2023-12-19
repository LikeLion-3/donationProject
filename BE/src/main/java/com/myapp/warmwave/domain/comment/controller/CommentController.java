package com.myapp.warmwave.domain.comment.controller;

import com.myapp.warmwave.domain.comment.dto.CommentRequestDto;
import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import com.myapp.warmwave.domain.comment.entity.Comment;
import com.myapp.warmwave.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/communities/{communityId}/comments")
public class CommentController {
    private final CommentService commentService;
    @PostMapping()
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable("communityId") Long communityId,
                                                            @RequestBody CommentRequestDto dto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        return new ResponseEntity<>(commentService.createComment(dto, communityId, userEmail), HttpStatus.CREATED);

    }

    @GetMapping() // 목록
    public ResponseEntity<Page<CommentResponseDto>> getComments(@PathVariable("communityId") Long communityId,
                                                                @RequestParam(required = false, defaultValue = "recent") String sort,
                                                                @PageableDefault Pageable pageable) {
        return new ResponseEntity<>(commentService.getComments(pageable, sort, communityId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody CommentRequestDto dto,
                                                            @PathVariable("commentId") Long commentId, @PathVariable Long communityId) {
        return new ResponseEntity<>(commentService.updateComment(dto, commentId, communityId), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId, @PathVariable Long communityId) {
        commentService.deleteComment(commentId, communityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
