package com.wooringpang.userservice.core.role.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooringpang.userservice.core.role.domain.QRoleAuthorization;
import com.wooringpang.userservice.core.role.dto.AuthorizationListDto;
import com.wooringpang.userservice.core.role.dto.AuthorizationSearchCondition;
import com.wooringpang.userservice.core.user.domain.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wooringpang.userservice.core.role.domain.QAuthorization.authorization;
import static com.wooringpang.userservice.core.role.domain.QRoleAuthorization.*;
import static com.wooringpang.userservice.core.user.domain.QUser.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class AuthorizationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public AuthorizationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 인가 페이지 목록 조회
     */
    public Page<AuthorizationListDto> findPageAuthorizations(AuthorizationSearchCondition condition, Pageable pageable) {

        //list
        List<AuthorizationListDto> content = getAuthorizationListJPQLQuery()
                .where(
                        searchKeywordContains(condition),
                        searchUrlPatternValue(condition.getSearchUrlPatternValue()),
                        searchHttpMethodCode(condition.getSearchHttpMethodCode())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //count
        JPAQuery<Long> count = queryFactory
                .select(authorization.count())
                .from(authorization)
                .where(
                        searchKeywordContains(condition),
                        searchUrlPatternValue(condition.getSearchUrlPatternValue()),
                        searchHttpMethodCode(condition.getSearchHttpMethodCode())
                );

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 권한 목록의 인가 전테 목록 조회
     */
    public List<AuthorizationListDto> findByRoles(List<String> roles) {
        return getAuthorizationListJPQLQuery()
                .where(
                        JPAExpressions
                                .select(roleAuthorization)
                                .from(roleAuthorization)
                                .where(
                                        roleAuthorization.roleAuthorizationId.authorizationId.eq(authorization.id)
                                                .and(roleAuthorization.roleAuthorizationId.roleCode.in(roles))
                                )
                                .exists()
                )
                .fetch();
    }

    /**
     * 사용자의 인가 목록 조회
     */
    public List<AuthorizationListDto> findByUserId(String signId) {
        return getAuthorizationListJPQLQuery()
                .where(
                        JPAExpressions
                                .select(roleAuthorization)
                                .innerJoin(user)
                                .on(user.role.stringValue().eq(roleAuthorization.roleAuthorizationId.roleCode))
                                .where(
                                        roleAuthorization.roleAuthorizationId.authorizationId.eq(authorization.id)
                                                .and(user.signId.eq(signId))
                                )
                                .exists()
                )
                .fetch();
    }

    /**
     * 인가 다음 정렬 순서 조회
     */
    public Integer findNextSort() {
        return queryFactory
                .select(authorization.sort.max().add(1).coalesce(1))
                .from(authorization)
                .fetchOne();
    }

    /**
     * 인가 정렬 순서 수정
     */
    public Long updateSort(Integer startSort, Integer endSort, int increaseSort) {
        return queryFactory
                .update(authorization)
                .set(authorization.sort, authorization.sort.add(increaseSort))
                .where(
                        isGoeSort(startSort),
                        isLoeSort(endSort)
                )
                .execute();
    }

    /**
     * 인가 목록 JPQL Query
     */
    private JPAQuery<AuthorizationListDto> getAuthorizationListJPQLQuery() {
        return queryFactory
                .select(
                        Projections.constructor(
                                AuthorizationListDto.class,
                                authorization.id,
                                authorization.name,
                                authorization.urlPatternValue,
                                authorization.httpMethodCode,
                                authorization.sort
                        )
                )
                .from(authorization);
    }

    /**
     * where searchKeywordType like '%searchKeyword%'
     * @param condition
     * @return
     */
    private BooleanExpression searchKeywordContains(AuthorizationSearchCondition condition) {
        if (condition.getSearchKeyword() == null || !hasText(condition.getSearchKeyword())) return null;

        switch (condition.getSearchKeywordType()) {
            case NAME -> authorization.name.containsIgnoreCase(condition.getSearchKeyword());
        }
        return null;
    }

    /**
     * where urlPatternValue like '%searchUrlPatternValue%'
     */
    private BooleanExpression searchUrlPatternValue(String searchUrlPatternValue) {
        return hasText(searchUrlPatternValue) ? authorization.urlPatternValue.containsIgnoreCase(searchUrlPatternValue) : null;
    }

    /**
     * where httpMethodCode like '%searchHttpMethodCode%'
     */
    private BooleanExpression searchHttpMethodCode(String searchHttpMethodCode) {
        return hasText(searchHttpMethodCode) ? authorization.httpMethodCode.containsIgnoreCase(searchHttpMethodCode) : null;
    }

    private BooleanExpression isGoeSort(Integer startSort) {
        return startSort == null ? null : authorization.sort.loe(startSort);
    }

    private BooleanExpression isLoeSort(Integer endSort) {
        return endSort == null ? null : authorization.sort.goe(endSort);
    }
}
