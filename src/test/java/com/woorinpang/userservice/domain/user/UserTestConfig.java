package com.woorinpang.userservice.domain.user;

import com.woorinpang.userservice.domain.user.infrastructure.UserQueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserTestConfig {

    @PersistenceContext protected EntityManager em;

    @Bean
    protected UserQueryRepository userQueryRepository() {
        return new UserQueryRepository(em);
    }
}
