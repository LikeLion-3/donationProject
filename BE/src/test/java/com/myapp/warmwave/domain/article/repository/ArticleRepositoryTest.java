package com.myapp.warmwave.domain.article.repository;

import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.config.JpaConfig;
import com.myapp.warmwave.config.QuerydslConfig;
import com.myapp.warmwave.domain.article.entity.Article;
import com.myapp.warmwave.domain.article.entity.ArticleType;
import com.myapp.warmwave.domain.article.entity.Status;
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
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository<User> userRepository;

    private Individual individual;
    private Institution institution;

    private Article articleIndividual() {
        return Article.builder()
                .id(1L)
                .user(individual)
                .title("제목1")
                .content("내용1")
                .articleStatus(Status.DEFAULT)
                .articleType(ArticleType.DONATION)
                .hit(0L)
                .userIp("111.111.111.111")
                .build();
    }

    private Article articleInstitution() {
        return Article.builder()
                .id(2L)
                .user(institution)
                .title("제목2")
                .content("내용2")
                .articleStatus(Status.DEFAULT)
                .articleType(ArticleType.BENEFICIARY)
                .hit(0L)
                .userIp("123.123.123.123")
                .build();
    }

    private Article articleCertificate() {
        return Article.builder()
                .id(3L)
                .user(institution)
                .title("제목3")
                .content("내용3")
                .articleStatus(Status.DEFAULT)
                .articleType(ArticleType.CERTIFICATION)
                .hit(0L)
                .userIp("123.123.123.123")
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
                .registerNum("1234")
                .build());
    }

    // CREATE
    @DisplayName("게시글 작성 (개인)")
    @Test
    void writeArticleByIndividual() {
        // given
        Article article = articleIndividual();

        // when
        Article savedArticle = articleRepository.save(article);

        // then
        assertThat(article).isEqualTo(savedArticle);
        assertThat(article.getUser().getRole()).isEqualTo(Role.INDIVIDUAL);
        assertThat(article.getArticleType()).isEqualTo(ArticleType.DONATION);
        assertThat(savedArticle.getId()).isNotNull();
        assertThat(articleRepository.count()).isEqualTo(1);
    }

    @DisplayName("게시글 작성 (개인)")
    @Test
    void writeArticleByInstitution() {
        // given
        Article article = articleInstitution();

        // when
        Article savedArticle = articleRepository.save(article);

        // then
        assertThat(article).isEqualTo(savedArticle);
        assertThat(article.getUser().getRole()).isEqualTo(Role.INSTITUTION);
        assertThat(article.getArticleType()).isEqualTo(ArticleType.BENEFICIARY);
        assertThat(savedArticle.getId()).isNotNull();
        assertThat(articleRepository.count()).isEqualTo(1);
    }

    // READ
    @DisplayName("게시글 전체 조회")
    @Test
    void readAllArticles() {
        // given
        articleRepository.save(articleInstitution());
        articleRepository.save(articleCertificate());
        articleRepository.save(articleIndividual());

        // when
        List<Article> articleList = articleRepository.findAll();

        // then
        assertThat(articleList).hasSize(3);
    }

    @DisplayName("게시글 단일 조회(기부해요)")
    @Test
    void readArticleDonation() {
        // given
        Article article = articleRepository.save(articleIndividual());

        // when
        Optional<Article> foundArticle = articleRepository.findByTitle(article.getTitle());

        // then
        assertThat(foundArticle).isPresent();
        assertThat(foundArticle.get()).isEqualTo(article);
        assertThat(foundArticle.get().getArticleType()).isEqualTo(ArticleType.DONATION);
    }

    @DisplayName("게시글 단일 조회(기부원해요)")
    @Test
    void readArticleBeneficiary() {
        // given
        Article article = articleRepository.save(articleInstitution());

        // when
        Optional<Article> foundArticle = articleRepository.findByTitle(article.getTitle());

        // then
        assertThat(foundArticle).isPresent();
        assertThat(foundArticle.get()).isEqualTo(article);
        assertThat(foundArticle.get().getArticleType()).isEqualTo(ArticleType.BENEFICIARY);
    }

    @DisplayName("게시글 단일 조회(인증해요)")
    @Test
    void readArticleCertification() {
        // given
        Article article = articleRepository.save(articleCertificate());

        // when
        Optional<Article> foundArticle = articleRepository.findByTitle(article.getTitle());

        // then
        assertThat(foundArticle).isPresent();
        assertThat(foundArticle.get()).isEqualTo(article);
        assertThat(foundArticle.get().getArticleType()).isEqualTo(ArticleType.CERTIFICATION);
    }

    // UPDATE
    // TODO 전체적인 로직 손봐야할 필요 있음 -> Mapper 쓰는 방향 말고 DTO 에서 바로 변환하는 방식?

    // DELETE
    @DisplayName("게시물 삭제")
    @Test
    void deleteArticle() {
        // given
        Article articleIndividual = articleRepository.save(articleIndividual());
        Article articleInstitution = articleRepository.save(articleIndividual());

        // when
        articleRepository.delete(articleIndividual);
        articleRepository.delete(articleInstitution);

        Optional<Article> foundArticleIndividual = articleRepository.findByTitle(articleIndividual.getTitle());
        Optional<Article> foundArticleInstitution = articleRepository.findByTitle(articleInstitution.getTitle());

        // then
        assertThat(foundArticleIndividual).isEmpty();
        assertThat(foundArticleInstitution).isEmpty();
    }
}
