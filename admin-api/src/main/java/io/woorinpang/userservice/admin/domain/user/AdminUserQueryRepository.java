package io.woorinpang.userservice.admin.domain.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.woorinpang.userservice.admin.domain.user.QAdminUser.*;
import static org.springframework.util.StringUtils.*;

@Repository
@RequiredArgsConstructor
public class AdminUserQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<AdminUser> findUsers(AdminUserSearchCondition condition, Pageable pageable) {
        return PageableExecutionUtils.getPage(
                getUserList(condition, pageable),
                pageable,
                getUserListCount(condition)::fetchOne
        );
    }

    private List<AdminUser> getUserList(AdminUserSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(adminUser)
                .from(adminUser)
                .where(
                        nameContainsIgnoreCase(condition.searchName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPAQuery<Long> getUserListCount(AdminUserSearchCondition condition) {
        return queryFactory.select(adminUser.count())
                .from(adminUser)
                .where(
                        nameContainsIgnoreCase(condition.searchName())
                );
    }

    private BooleanExpression nameContainsIgnoreCase(String name) {
        return !hasText(name) ? null : adminUser.name.containsIgnoreCase(name);
    }
}
