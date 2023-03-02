package com.wooringpang.core.log.repository;

import com.wooringpang.core.log.domain.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
