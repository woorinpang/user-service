package com.woorinpang.userservice.test.config;

import com.woorinpang.userservice.domain.user.infrastructure.UserQueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RepositoryTestConfig {
    @PersistenceContext private EntityManager em;

    //사용자
    @Bean
    public UserQueryRepository userQueryRepository() {
        return new UserQueryRepository(em);
    }
}
