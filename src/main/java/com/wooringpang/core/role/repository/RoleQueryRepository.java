package com.wooringpang.core.role.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wooringpang.core.role.dto.RoleListDto;
import com.wooringpang.core.role.dto.RoleSearchCondition;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wooringpang.userservice.core.role.domain.QRole.*;
import static org.springframework.util.StringUtils.*;

@Repository
public class RoleQueryRepository {

    private final JPAQueryFactory queryFactory;

    public RoleQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 권한 페이지 목록 조회
     */
    public Page<RoleListDto> findPageRoles(RoleSearchCondition condition, Pageable pageable) {

        List<RoleListDto> content = getRoleList(condition, pageable);

        JPAQuery<Long> count = getRoleListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 권한 목록
     */
    private List<RoleListDto> getRoleList(RoleSearchCondition condition, Pageable pageable) {
        List<RoleListDto> content = queryFactory
                .select(
                        Projections.constructor(
                                RoleListDto.class,
                                role.code,
                                role.name,
                                role.content,
                                role.createdDate
                        )
                )
                .from(role)
                .where(searchKeywordContains(condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(role.sort.asc())
                .fetch();
        return content;
    }

    /**
     * 권한 목록 카운트
     */
    private JPAQuery<Long> getRoleListCount(RoleSearchCondition condition) {
        JPAQuery<Long> count = queryFactory
                .select(role.count())
                .from(role)
                .where(searchKeywordContains(condition));
        return count;
    }

    /**
     * where searchKeywordType like '%searchKeyword%'
     */
    private BooleanExpression searchKeywordContains(RoleSearchCondition condition) {
        if (condition.getSearchKeywordType() == null || !hasText(condition.getSearchKeyword())) return null;

        switch (condition.getSearchKeywordType()) {
            case CODE -> role.code.containsIgnoreCase(condition.getSearchKeyword());
            case NAME -> role.name.containsIgnoreCase(condition.getSearchKeyword());
            case CONTENT -> role.content.containsIgnoreCase(condition.getSearchKeyword());
        }
        return null;
    }
}
