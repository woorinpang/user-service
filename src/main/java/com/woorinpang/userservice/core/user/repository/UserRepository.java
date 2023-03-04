package com.woorinpang.userservice.core.user.repository;

import com.woorinpang.userservice.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndUsernameNot(String email, String username);

    Optional<User> findByGoogleId(String providerId);

    Optional<User> findByNaverId(String providerId);

    Optional<User> findByKakaoId(String providerId);

    Optional<User> findByEmailAndName(String email, String userName);
}
