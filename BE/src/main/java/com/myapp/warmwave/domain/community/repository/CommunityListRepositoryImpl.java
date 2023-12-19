package com.myapp.warmwave.domain.community.repository;

import com.myapp.warmwave.domain.community.dto.CommunityListProjectionDto;
import com.myapp.warmwave.domain.community.dto.CommunityListResponseDto;
import com.myapp.warmwave.domain.community.entity.Community;
import com.myapp.warmwave.domain.user.dto.CacheUserDto;
import com.myapp.warmwave.domain.user.service.UserService;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.myapp.warmwave.domain.community.entity.QCommunity.community;
import static com.myapp.warmwave.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommunityListRepositoryImpl implements CommunityListRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserService userService;

    @Override
    public Page<CommunityListResponseDto> findAllCommunities(Pageable pageable, String sort) {
        OrderSpecifier<?> orderBy = community.createdAt.desc();
        if ("popular".equals(sort)) {
            orderBy = community.hit.desc();
        }

        List<CommunityListProjectionDto> projections = jpaQueryFactory
                .select(
                        Projections.constructor(CommunityListProjectionDto.class,
                                community.id,
                                community.title,
                                community.hit,
                                community.user.id,
                                new CaseBuilder()
                                        .when(community.communityCategory.eq(Community.CommunityCategory.valueOf("volunteer_recruit")))
                                        .then("봉사모집")
                                        .when(community.communityCategory.eq(Community.CommunityCategory.valueOf("volunteer_certificate")))
                                        .then("봉사인증")
                                        .when(community.communityCategory.eq(Community.CommunityCategory.valueOf("etc")))
                                        .then("잡다구리")
                                        .when(community.communityCategory.eq(Community.CommunityCategory.valueOf("notice")))
                                        .then("공지사항")
                                        .otherwise("기타"),
                                community.createdAt)
                )
                .from(community)
                .leftJoin(community.user, user)
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(orderBy)
                .fetch();

        List<CommunityListResponseDto> result = new ArrayList<>();
        for (CommunityListProjectionDto projection : projections) {
            CacheUserDto userCacheDTO = userService.findUserCacheDtoById(projection.getUserId());
            String writer = userCacheDTO.getName();
            result.add(new CommunityListResponseDto(
                    projection.getId(),
                    projection.getTitle(),
                    projection.getHit(),
                    writer,
                    projection.getCategory(),
                    projection.getCreatedAt()
            ));
        }

        Long total = jpaQueryFactory
                .select(community.count())
                .from(community)
                .fetchOne();

        return new PageImpl(result, pageable, total == null ? 0 : total);
    }
}