package com.woorinpang.userservice.core.role.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woorinpang.userservice.core.role.dto.QRoleAuthorizationListDto;
import com.woorinpang.userservice.core.role.dto.RoleAuthorizationListDto;
import com.woorinpang.userservice.core.role.dto.RoleAuthorizationSearchCondition;
import com.woorinpang.userservice.global.dto.CommonSearchCondition;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.woorinpang.userservice.core.role.domain.QAuthorization.authorization;
import static com.woorinpang.userservice.core.role.domain.QRoleAuthorization.roleAuthorization;
import static org.springframework.util.StringUtils.*;

@Repository
public class RoleAuthorizationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public RoleAuthorizationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 권한 인가 페이지 목록 조회
     */
    public Page<RoleAuthorizationListDto> findPageRoleAuthorizations(RoleAuthorizationSearchCondition condition, Pageable pageable) {
        List<RoleAuthorizationListDto> content = queryFactory
                .select(
                        new QRoleAuthorizationListDto(
                                Expressions.as(Expressions.constant(condition.getRoleCode()), "roleCode"),
                                authorization.id,
                                authorization.name,
                                authorization.urlPatternValue,
                                authorization.httpMethodCode,
                                authorization.sort,
                                Expressions.as(new CaseBuilder()
                                        .when(roleAuthorization.roleAuthorizationId.roleCode.isNotNull()
                                                .and(roleAuthorization.roleAuthorizationId.authorizationId.isNotNull()))
                                        .then(true)
                                        .otherwise(false), "createdAt"
                                )
                        )
                )
                .from(authorization)
                .leftJoin(roleAuthorization).on(authorization.id.eq(roleAuthorization.roleAuthorizationId.authorizationId)
                        .and(roleCodeEq(condition.getRoleCode()))
                )
                .fetchJoin()
                .where(
                        searchKeywordContains(condition),
                        searchUrlPatternValueContains(condition.getSearchUrlPatternValue()),
                        searchHttpMethodCodeContains(condition.getSearchHttpMethodCode())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(authorization.sort.asc())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(authorization.count())
                .from(authorization)
                .leftJoin(roleAuthorization).on(authorization.id.eq(roleAuthorization.roleAuthorizationId.authorizationId)
                        .and(roleCodeEq(condition.getRoleCode()))
                )
                .fetchJoin()
                .where(
                        searchKeywordContains(condition),
                        searchUrlPatternValueContains(condition.getSearchUrlPatternValue()),
                        searchHttpMethodCodeContains(condition.getSearchHttpMethodCode())
                );

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    private BooleanExpression roleCodeEq(String roleCode) {
        return hasText(roleCode) ? roleAuthorization.roleAuthorizationId.roleCode.eq(roleCode) : null;
    }

    /**
     * where searchKeywordType like '%searchKeyword%'
     */
    private BooleanExpression searchKeywordContains(RoleAuthorizationSearchCondition condition) {
        if(condition.getSearchKeywordType() == null || !StringUtils.hasText(condition.getSearchKeyword())) return null;

        switch (condition.getSearchKeywordType()) {
            case NAME -> authorization.name.containsIgnoreCase(condition.getSearchKeyword());
        }
        return null;
    }

    /**
     * where urlPatternValue like '%searchUrlPatternValue%'
     */
    private BooleanExpression searchUrlPatternValueContains(String searchUrlPatternValue) {
        return hasText(searchUrlPatternValue) ? authorization.urlPatternValue.containsIgnoreCase(searchUrlPatternValue) : null;
    }

    /**
     * where httpMethodCode like '%searchHttpMethodCode%'
     */
    private BooleanExpression searchHttpMethodCodeContains(String searchHttpMethodCode) {
        return hasText(searchHttpMethodCode) ? authorization.httpMethodCode.containsIgnoreCase(searchHttpMethodCode) : null;
    }
}

