package com.wooringpang.userservice.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooringpang.userservice.domain.user.dto.UserListDto;
import com.wooringpang.userservice.domain.user.dto.UserSearchCondition;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wooringpang.userservice.domain.user.entity.QUser.*;
import static org.springframework.util.StringUtils.*;

@Repository
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 유저 목록 조회
     */
    public Page<UserListDto> findUsers(UserSearchCondition condition, Pageable pageable) {

        List<UserListDto> content = getUserList(condition, pageable);

        JPAQuery<Long> count = getUserListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 유저 목록
     */
    private List<UserListDto> getUserList(UserSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(
                        Projections.constructor(
                                UserListDto.class,
                                user.userId,
                                user.username,
                                user.email,
                                user.role,
                                user.userStateCode,
                                user.lastLoginDate,
                                user.loginFailCount
                        )
                )
                .from(user)
                .where(searchKeywordContains(condition))
                .orderBy(user.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 유저 목록 카운트
     */
    private JPAQuery<Long> getUserListCount(UserSearchCondition condition) {
        return queryFactory
                .select(user.count())
                .from(user)
                .where(searchKeywordContains(condition));
    }

    /**
     * where searchKeywordType like '%searchKeyword%'
     */
    private BooleanExpression searchKeywordContains(UserSearchCondition condition) {
        if (condition.getSearchKeywordType() == null || !hasText(condition.getSearchKeyword())) return null;

        switch (condition.getSearchKeywordType()) {
            case NAME -> {
                return user.username.containsIgnoreCase(condition.getSearchKeyword());
            }
            case EMAIL -> {
                return user.email.containsIgnoreCase(condition.getSearchKeyword());
            }
            default -> {
                return null;
            }
        }
    }
}
