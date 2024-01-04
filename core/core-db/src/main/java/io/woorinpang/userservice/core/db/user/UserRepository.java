package io.woorinpang.userservice.core.db.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByEmailAndUsernameNot(String email, String username);
    Optional<User> findByGoogleId(String providerId);
    Optional<User> findByNaverId(String providerId);
    Optional<User> findByKakaoId(String providerId);


}
