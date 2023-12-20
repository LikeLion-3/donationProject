package com.myapp.warmwave.domain.comment.repository;

import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.exception.CustomExceptionCode;
import com.myapp.warmwave.domain.comment.dto.CommentProjectionDto;
import com.myapp.warmwave.domain.comment.dto.CommentResponseDto;
import com.myapp.warmwave.domain.user.dto.CacheUserDto;
import com.myapp.warmwave.domain.user.entity.User;
import com.myapp.warmwave.domain.user.repository.UserRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.myapp.warmwave.domain.comment.entity.QComment.comment;
import static com.myapp.warmwave.domain.community.entity.QCommunity.community;
import static com.myapp.warmwave.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommentListRepositoryImpl implements CommentListRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserRepository<User> userRepository;

    @Override
    public Page<CommentResponseDto> findComments(Pageable pageable, String sort, Long communityId) {

        OrderSpecifier<?> orderBy = comment.createdAt.desc();
        if ("popular".equals(sort)) {
            orderBy = comment.createdAt.desc(); // 추후 좋아요 기능 구현 가능성..
        }
        List<CommentProjectionDto> projections = jpaQueryFactory
                .select(
                        Projections.constructor(CommentProjectionDto.class,
                                comment.id,
                                comment.contents,
                                comment.createdAt,
                                comment.modifiedAt,
                                community.user.id,
                                comment.community.id)
                )
                .from(comment)
                .leftJoin(comment.community.user, user)
                .where(comment.community.id.eq(communityId))
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(orderBy)
                .fetch();

        List<CommentResponseDto> result = new ArrayList<>();
        for (CommentProjectionDto projection : projections) {
            CacheUserDto userCacheDTO = userRepository.findById(projection.getId())
                    .map(usr1 -> new CacheUserDto(usr1.getId(), usr1.getName()))
                    .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_USER));

            String writer = userCacheDTO.getName();
            result.add(new CommentResponseDto(
                    projection.getId(),
                    projection.getContents(),
                    projection.getCreatedAt(),
                    projection.getModifiedAt(),
                    writer,
                    projection.getCommunityId(),
                    projection.getUserId()
            ));
        }
        Long total = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .fetchOne();
        System.out.println("result : " + result);
        return new PageImpl(result, pageable, total==null ? 0:total);
    }
}
