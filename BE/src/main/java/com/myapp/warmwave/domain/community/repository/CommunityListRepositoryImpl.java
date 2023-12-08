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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    @Override
    public Slice<CommunityListResponseDto> findAllCommunities(Pageable pageable) {

        List<CommunityListResponseDto> result = jpaQueryFactory
                .select(
                        Projections.constructor(CommunityListResponseDto.class,
                                community.id,
                                community.title,
                                community.hit,
//                              individual.nickname -> individual 테이블이 있거나 User에 name 필드가 있어야 함
                                new CaseBuilder()
                                        .when(user.instanceOf(Individual.class)).then("개인")
                                        .when(user.instanceOf(Institution.class)).then("기관")
                                        .otherwise("Unknown").as("writer"),

                                community.category,
                                community.createdAt)
                )
                .from(community)
                .leftJoin(community.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(community.createdAt.desc())
                .fetch();

        boolean hasNext = false;

        if (result.size() > pageable.getPageSize()) {
            result.remove(result.size() - 1);
            hasNext = true;
        }
        return new SliceImpl(result, pageable, hasNext);
    }
}