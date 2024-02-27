package io.woorinpang.userservice.storage.core.db.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "io.woorinpang.userservice.storage.core.db")
@EnableJpaRepositories(basePackages = "io.woorinpang.userservice.storage.core.db")
public class CoreJpaConfig {
}
