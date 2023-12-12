package com.myapp.warmwave.domain.chat.repository;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.config.QuerydslConfig;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ArticleType;
import com.myapp.warmwave.domain.article.entity.Status;
import com.myapp.warmwave.domain.article.repository.ArticleRepository;
import com.myapp.warmwave.domain.chat.entity.ChatRoom;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChatroomRepositoryTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository<User> userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private Individual individual;
    private Institution institution;
    private Article article;

    private ChatRoom chatRoom() {
        return ChatRoom.builder()
                .id(1L)
                .donor(individual)
                .recipient(institution)
                .article(article)
                .status("상태1")
                .deletedAt(null)
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

        institution = userRepository.save(Institution.builder()
                .id(2L)
                .email("email2")
                .password("12345")
                .role(Role.INSTITUTION)
                .isApprove(true)
                .institutionName("기관1")
                .build());

        article = articleRepository.save(Article.builder()
                .id(1L)
                .user(individual)
                .title("제목1")
                .content("내용1")
                .articleStatus(Status.DEFAULT)
                .articleType(ArticleType.DONATION)
                .hit(0L)
                .build());
    }

    @DisplayName("채팅방 개설")
    @Test
    void createChatroom() {
        // given
        ChatRoom chatRoom = chatRoom();

        // when
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // then
        assertThat(savedChatRoom).isNotNull();
        assertThat(chatRoomRepository.count()).isEqualTo(1);
    }

    @DisplayName("채팅방 목록 조회")
    @Test
    void readAllChatRoom() {
        // given
        chatRoomRepository.save(chatRoom());

        // when
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();

        // then
        assertThat(chatRoomList).hasSize(1);
    }

    @DisplayName("채팅방 삭제")
    @Test
    void deleteChatRoom() {
        // given
        ChatRoom chatRoom = chatRoomRepository.save(chatRoom());

        // when
        chatRoomRepository.delete(chatRoom);

        Optional<ChatRoom> foundChatRoom = chatRoomRepository.findById(1L);

        // then
        assertThat(foundChatRoom).isEmpty();
    }
}
