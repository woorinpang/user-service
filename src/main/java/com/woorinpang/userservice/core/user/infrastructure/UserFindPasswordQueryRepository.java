package com.woorinpang.userservice.core.user.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.woorinpang.userservice.core.user.domain.QUserFindPassword.userFindPassword;

@Repository
public class UserFindPasswordQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserFindPasswordQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Integer findNextRequestNo(String email) {
        return queryFactory
                .select(userFindPassword.userFindPasswordId.requestNo.max().add(1).coalesce(1))
                .from(userFindPassword)
                .where(userFindPassword.userFindPasswordId.email.eq(email))
                .fetchOne();
    }
}
