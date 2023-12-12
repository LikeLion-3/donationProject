package com.myapp.warmwave.domain.comment.repository;

import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import com.myapp.warmwave.domain.user.entity.Individual;
import com.myapp.warmwave.domain.user.entity.Institution;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.myapp.warmwave.domain.comment.entity.QComment.comment;
import static com.myapp.warmwave.domain.community.entity.QCommunity.community;
import static com.myapp.warmwave.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommentListRepositoryImpl implements CommentListRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentResponseDto> findComments(Pageable pageable, String sort, Long communityId) {

        OrderSpecifier<?> orderBy = comment.createdAt.desc();
        if ("popular".equals(sort)) {
            orderBy = comment.createdAt.desc(); // 추후 좋아요 기능 구현 가능성..
        }
        List<CommentResponseDto> result = jpaQueryFactory
                .select(
                        Projections.constructor(CommentResponseDto.class,
                                comment.id,
                                comment.contents,
                                comment.createdAt,
                                comment.modifiedAt,
                                new CaseBuilder()
                                        .when(user.instanceOf(Individual.class)).then("개인")
                                        .when(user.instanceOf(Institution.class)).then("기관")
                                        .otherwise("Unknown").as("writer"),
                                comment.community.id)
                )
                .from(comment)
                .leftJoin(comment.community.user, user)
                .where(comment.community.id.eq(communityId))
                .orderBy(orderBy)
                .fetch();

        Long total = jpaQueryFactory
                .select(community.count())
                .from(community)
                .fetchOne();

        return new PageImpl(result, pageable, total==null ? 0:total);
    }
}
