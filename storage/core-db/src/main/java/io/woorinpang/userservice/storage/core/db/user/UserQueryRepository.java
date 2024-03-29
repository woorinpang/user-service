package io.woorinpang.userservice.storage.core.db.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.woorinpang.userservice.core.enums.user.UserRole;
import io.woorinpang.userservice.core.enums.user.UserState;
import io.woorinpang.userservice.storage.core.db.user.dto.FindPageUserProjection;
import io.woorinpang.userservice.storage.core.db.user.dto.UserSearchCondition;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static io.woorinpang.userservice.storage.core.db.user.QUserEntity.userEntity;

@Repository
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 유저 목록 조회
     */
    public Page<FindPageUserProjection> findPageUser(UserSearchCondition condition, Pageable pageable) {
        return PageableExecutionUtils.getPage(
                getUserList(condition, pageable),
                pageable,
                getUserListCount(condition)::fetchOne
        );
    }

    /**
     * 유저 목록
     */
    private List<FindPageUserProjection> getUserList(UserSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(
                        Projections.constructor(
                                FindPageUserProjection.class,
                                userEntity.id,
                                userEntity.username,
                                userEntity.email,
                                userEntity.name,
                                userEntity.role,
                                userEntity.state
                        )
                )
                .from(userEntity)
                .where(
                        searchKeywordContains(condition),
                        searchRoleEq(condition.getSearchRole()),
                        searchUserStateEq(condition.getSearchUserState())
                )
                .orderBy(userEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 유저 목록 카운트
     */
    private JPAQuery<Long> getUserListCount(UserSearchCondition condition) {
        return queryFactory
                .select(userEntity.count())
                .from(userEntity)
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
            case NAME -> userEntity.name.containsIgnoreCase(condition.getSearchKeyword());
            case EMAIL -> userEntity.email.containsIgnoreCase(condition.getSearchKeyword());
            default -> null;
        };
    }

    /**
     * where role = searchRole
     */
    private BooleanExpression searchRoleEq(UserRole searchUserRole) {
        return searchUserRole != null ? userEntity.role.eq(searchUserRole) : null;
    }

    /**
     * where userstate = searchUserState
     */
    private BooleanExpression searchUserStateEq(UserState searchUserState) {
        return searchUserState != null ? userEntity.state.eq(searchUserState) : null;
    }
}
