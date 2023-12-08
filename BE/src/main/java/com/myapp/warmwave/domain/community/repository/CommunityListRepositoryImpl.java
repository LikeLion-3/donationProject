package com.myapp.warmwave.domain.community.repository;

import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.dto.CommunityResponseDto;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.myapp.warmwave.domain.community.entity.QCommunity.community;
import static com.myapp.warmwave.domain.image.entity.QImage.image;
import static com.myapp.warmwave.domain.user.entity.QIndividual.individual;
import static com.myapp.warmwave.domain.user.entity.QInstitution.institution;
import static com.myapp.warmwave.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommunityListRepositoryImpl implements CommunityListRepository{

    private final JPAQueryFactory jpaQueryFactory;
    // Refactor : 코드 중복
    @Override
    public Page<CommunityListResponseDto> findAllCommunities(Pageable pageable) {
        List<CommunityListResponseDto> result = jpaQueryFactory
                .select(
                        Projections.constructor(CommunityListResponseDto.class,
                                community.id,
                                community.title,
                                community.hit,
                                new CaseBuilder()
                                        .when(user.instanceOf(Individual.class)).then("개인")
                                        .when(user.instanceOf(Institution.class)).then("기관")
                                        .otherwise("Unknown").as("writer"),

                                community.category,
                                community.createdAt)
                )
                .from(community)
                .leftJoin(community.user, user)
                // offset : 몇 번부터? 페이지 번호 = 1이면 .. 20부터자나!!
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(community.createdAt.desc())
                .fetch();

        Long total = jpaQueryFactory
                .select(community.count())
                .from(community)
                .fetchOne();

        return new PageImpl(result, pageable, total==null ? 0:total);
    }

    @Override
    public Page<CommunityListResponseDto> findAllCommunitiesOrderByHit(Pageable pageable) {
        List<CommunityListResponseDto> result = jpaQueryFactory
                .select(
                        Projections.constructor(CommunityListResponseDto.class,
                                community.id,
                                community.title,
                                community.hit,
                                new CaseBuilder()
                                        .when(user.instanceOf(Individual.class)).then("개인")
                                        .when(user.instanceOf(Institution.class)).then("기관")
                                        .otherwise("Unknown").as("writer"),

                                community.category,
                                community.createdAt)
                )
                .from(community)
                .leftJoin(community.user, user)
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(community.hit.desc())
                .fetch();

        Long total = jpaQueryFactory
                .select(community.count())
                .from(community)
                .fetchOne();

        return new PageImpl(result, pageable, total==null ? 0:total);
    }
}