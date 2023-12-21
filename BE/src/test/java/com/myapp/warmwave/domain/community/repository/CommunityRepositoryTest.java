package com.myapp.warmwave.domain.community.repository;

import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.config.QuerydslConfig;
import com.myapp.warmwave.domain.community.entity.Community;
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
class CommunityRepositoryTest {
    @Autowired
    private CommunityRepository communityRepository;

    private Community community() {
        return Community.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .communityCategory(Community.CommunityCategory.notice)
                .comments(new ArrayList<>())
                .images(new ArrayList<>())
                .hit(0)
                .build();
    }

    // CREATE
    @Test
    @DisplayName("커뮤니티 등록 확인")
    void addCommunity() {
        // given
        Community community = community();

        // when
        Community savedCommunity = communityRepository.save(community);

        // then
        assertThat(savedCommunity.getTitle()).isEqualTo(community.getTitle());
    }

    // READ
    @Test
    @DisplayName("커뮤니티 목록 조회 확인")
    void readAllCommunity() {
        // given
        communityRepository.save(community());

        // when
        List<Community> communityList = communityRepository.findAll();

        // then
        assertThat(communityList).hasSize(1);
    }

    @Test
    @DisplayName("커뮤니티 단일 조회 확인")
    void readCommunity() {
        // given
        Community community = communityRepository.save(community());

        // when
        Optional<Community> foundCommunity = communityRepository.findByTitle(community.getTitle());

        // then
        assertThat(foundCommunity).isPresent();
        assertThat(foundCommunity.get().getTitle()).isEqualTo(community.getTitle());
    }

    // UPDATE
    @Test
    @DisplayName("커뮤니티 수정 확인")
    void updateCommunity() {
        // given
        Community community = communityRepository.save(community());
        String originTitle = community.getTitle();
        community.setTitle("제목 수정");
        communityRepository.save(community);

        // when
        Optional<Community> foundCommunity = communityRepository.findById(community.getId());

        // then
        assertThat(foundCommunity).isPresent();
        assertThat(foundCommunity.get().getTitle()).isNotEqualTo(originTitle);
    }

    // DELETE
    @Test
    @DisplayName("커뮤니티 삭제 확인")
    void deleteCommunity() {
        // given
        Community community = communityRepository.save(community());

        // when
        communityRepository.delete(community);

        Optional<Community> foundCommunity = communityRepository.findById(community.getId());

        // then
        assertThat(foundCommunity).isEmpty();
    }
}
