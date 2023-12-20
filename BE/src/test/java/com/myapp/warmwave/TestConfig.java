package com.myapp.warmwave;

import com.myapp.warmwave.domain.comment.repository.CommentListRepositoryImpl;
import com.myapp.warmwave.domain.community.repository.CommunityListRepositoryImpl;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {
    private final UserRepository<User> userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public CommunityListRepositoryImpl communityListRepository() {
        return new CommunityListRepositoryImpl(jpaQueryFactory(), userRepository);
    }

    @Bean
    public CommentListRepositoryImpl commentListRepository() {
        return new CommentListRepositoryImpl(jpaQueryFactory(), userRepository);
    }
}
