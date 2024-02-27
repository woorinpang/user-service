package io.woorinpang.userservice.storage.core.db.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLogEntity, Long> {
}
