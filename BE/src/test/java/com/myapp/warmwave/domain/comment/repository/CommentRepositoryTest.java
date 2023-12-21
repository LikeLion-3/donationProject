package com.myapp.warmwave.domain.comment.repository;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.config.QuerydslConfig;
import com.myapp.warmwave.domain.comment.entity.Comment;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.community.repository.CommunityRepository;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository<User> userRepository;

    private Individual individual;
    private Community community;

    private Comment comment() {
        return Comment.builder()
                .id(1L)
                .community(community)
                .contents("댓글 내용")
                .userIp("123.123.123.123")
                .user(individual)
                .build();
    }

    @BeforeEach
    void setup() {
        individual = userRepository.save(Individual.builder()
                .id(1L)
                .email("email1")
                .password("1234")
                .role(Role.INDIVIDUAL)
                .nickname("닉네임1")
                .build());

        community = communityRepository.save(Community.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .communityCategory(Community.CommunityCategory.notice)
                .images(new ArrayList<>())
                .comments(new ArrayList<>())
                .build());
    }

    // CREATE
    @DisplayName("댓글 작성")
    @Test
    void create() {
        // given
        Comment comment = comment();

        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        assertThat(savedComment).isNotNull();
    }

    // READ
    @DisplayName("댓글 목록 조회")
    @Test
    void readAll() {
        // given
        commentRepository.save(comment());

        // when
        List<Comment> comments = commentRepository.findAll();

        // then
        assertThat(comments).hasSize(1);
    }

    @DisplayName("댓글 조회")
    @Test
    void read() {
        // given
        Comment comment = commentRepository.save(comment());

        // when
        Optional<Comment> foundComment = commentRepository.findById(comment.getId());

        // then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContents()).isEqualTo(comment.getContents());
    }

    // UPDATE
    @DisplayName("댓글 수정")
    @Test
    void update() {
        // given
        Comment comment = commentRepository.save(comment());
        String originalContents = comment.getContents();

        comment.updateComment("내용 수정");

        commentRepository.save(comment);

        // when
        Optional<Comment> foundComment = commentRepository.findById(comment.getId());

        // then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContents()).isNotEqualTo(originalContents);
    }

    // DELETE
    @DisplayName("댓글 삭제")
    @Test
    void delete() {
        // given
        Comment comment = commentRepository.save(comment());

        // when
        commentRepository.delete(comment);

        Optional<Comment> foundComment = commentRepository.findById(comment.getId());

        // then
        assertThat(foundComment).isEmpty();
    }
}
