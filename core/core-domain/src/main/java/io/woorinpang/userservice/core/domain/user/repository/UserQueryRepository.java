package io.woorinpang.userservice.core.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.woorinpang.userservice.core.domain.user.domain.FindPageUser;
import io.woorinpang.userservice.core.domain.user.domain.UserSearchCondition;
import io.woorinpang.userservice.core.domain.user.domain.UserRole;
import io.woorinpang.userservice.core.domain.user.domain.UserState;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static io.woorinpang.userservice.core.domain.user.domain.QUser.user;

@Repository
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 유저 목록 조회
     */
    public Page<FindPageUser> findPageUser(UserSearchCondition condition, Pageable pageable) {
        return PageableExecutionUtils.getPage(
                getUserList(condition, pageable),
                pageable,
                getUserListCount(condition)::fetchOne
        );
    }

    /**
     * 유저 목록
     */
    private List<FindPageUser> getUserList(UserSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(
                        Projections.constructor(
                                FindPageUser.class,
                                user.id,
                                user.email,
                                user.name,
                                user.role,
                                user.state
                        )
                )
                .from(user)
                .where(
                        searchKeywordContains(condition),
                        searchRoleEq(condition.getSearchRole()),
                        searchUserStateEq(condition.getSearchUserState())
                )
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
                .where(
                        searchKeywordContains(condition),
                        searchRoleEq(condition.getSearchRole()),
                        searchUserStateEq(condition.getSearchUserState())
                );
    }

    /**
     * where searchKeywordType like '%searchKeyword%'
     */
    private BooleanExpression searchKeywordContains(UserSearchCondition condition) {
        if (condition.getSearchKeywordType() == null || !StringUtils.hasText(condition.getSearchKeyword())) return null;

        return switch (condition.getSearchKeywordType()) {
            case NAME -> user.name.containsIgnoreCase(condition.getSearchKeyword());
            case EMAIL -> user.email.containsIgnoreCase(condition.getSearchKeyword());
            default -> null;
        };
    }

    /**
     * where role = searchRole
     */
    private BooleanExpression searchRoleEq(UserRole searchUserRole) {
        return searchUserRole != null ? user.role.eq(searchUserRole) : null;
    }

    /**
     * where userstate = searchUserState
     */
    private BooleanExpression searchUserStateEq(UserState searchUserState) {
        return searchUserState != null ? user.state.eq(searchUserState) : null;
    }
}
