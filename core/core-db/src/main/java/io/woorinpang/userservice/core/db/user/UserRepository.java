package io.woorinpang.userservice.core.db.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByRefreshToken(String refreshToken);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
