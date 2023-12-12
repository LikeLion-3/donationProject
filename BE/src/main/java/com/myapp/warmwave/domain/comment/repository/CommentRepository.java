package com.myapp.warmwave.domain.comment.repository;

import com.myapp.warmwave.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentListRepository {
}
