package com.myapp.warmwave.domain.favorite.repository;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.config.QuerydslConfig;
import com.myapp.warmwave.domain.email.entity.EmailAuth;
import com.myapp.warmwave.domain.favorite_inst.entity.FavoriteInst;
import com.myapp.warmwave.domain.favorite_inst.repository.FavoriteInstRepository;
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

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({JpaConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FavoriteInstRepositoryTest {
    @Autowired
    private FavoriteInstRepository favoriteInstRepository;

    @Autowired
    private UserRepository<User> userRepository;

    private Individual individual;
    private Institution institution;
    private EmailAuth emailAuth1;
    private EmailAuth emailAuth2;

    private FavoriteInst favoriteInst() {
        return FavoriteInst.builder()
                .id(1L)
                .individualUser(individual)
                .institutionUser(institution)
                .build();
    }

    @BeforeEach
    void setup() {
        individual = userRepository.save(Individual.builder()
                .id(1L)
                .email("email1")
                .password("1234")
                .role(Role.INDIVIDUAL)
                .emailAuth(emailAuth1)
                .nickname("닉네임1")
                .build());

        institution = userRepository.save(Institution.builder()
                .id(2L)
                .email("email2")
                .password("12345")
                .role(Role.INSTITUTION)
                .institutionName("기관1")
                .emailAuth(emailAuth2)
                .build());

        emailAuth1 = EmailAuth.builder()
                .id(1L)
                .build();

        emailAuth2 = EmailAuth.builder()
                .id(2L)
                .build();
    }

    // CREATE
    @DisplayName("관심 기관 등록")
    @Test
    void addFavorite() {
        // given
        FavoriteInst favoriteInst = favoriteInst();

        // when
        FavoriteInst savedFavorite =
                favoriteInstRepository.save(favoriteInst);

        // then
        assertThat(savedFavorite).isEqualTo(favoriteInst);
        assertThat(savedFavorite.getId()).isNotNull();
        assertThat(favoriteInstRepository.count()).isEqualTo(1);
    }

    // READ
    @DisplayName("관심 기관 목록 조회")
    @Test
    void readAllFavorites() {
        // given
        favoriteInstRepository.save(favoriteInst());

        // when
        List<FavoriteInst> favoriteInstList =
                favoriteInstRepository.findAll();

        // then
        assertThat(favoriteInstList).hasSize(1);
    }

    @DisplayName("관심 기관 단일 조회")
    @Test
    void readFavorite() {
        // given
        FavoriteInst favoriteInst =
                favoriteInstRepository.save(favoriteInst());

        // when
        Optional<FavoriteInst> foundFavorite =
                favoriteInstRepository.findByIndividualUser_Email(
                        favoriteInst.getIndividualUser().getEmail()
                );

        // then
        assertThat(foundFavorite).isPresent();
        assertThat(foundFavorite.get()).isEqualTo(favoriteInst);
    }

    // DELETE
    @DisplayName("관심 기관 삭제")
    @Test
    void deleteFavorite() {
        // given
        FavoriteInst favoriteInst =
                favoriteInstRepository.save(favoriteInst());

        // when
        favoriteInstRepository.delete(favoriteInst);

        Optional<FavoriteInst> foundFavorite =
                favoriteInstRepository.findById(1L);

        // then
        assertThat(foundFavorite).isEmpty();
    }
}
