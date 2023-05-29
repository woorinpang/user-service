package com.woorinpang.userservice.test;

import com.woorinpang.userservice.domain.user.infrastructure.UserQueryRepository;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.global.config.jpa.JpaAuditingConfig;
import com.woorinpang.userservice.global.config.p6spy.P6SpyConfig;
import com.woorinpang.userservice.test.config.RepositoryTestConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {JpaAuditingConfig.class, P6SpyConfig.class}
))
@Import(RepositoryTestConfig.class)
@ActiveProfiles(profiles = "test")
public class RepositoryTest {
    @PersistenceContext protected EntityManager em;

    @Autowired protected UserQueryRepository userQueryRepository;
    @Autowired protected UserRepository userRepository;
}
