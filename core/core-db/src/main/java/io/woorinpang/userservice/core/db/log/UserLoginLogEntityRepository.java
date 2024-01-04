package io.woorinpang.userservice.core.db.log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogEntityRepository extends JpaRepository<UserLoginLogEntity, Long> {
}
