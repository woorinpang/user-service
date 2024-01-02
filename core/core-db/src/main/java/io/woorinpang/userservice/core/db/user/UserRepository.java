package io.woorinpang.userservice.core.db.user;

import com.woorinpang.userservice.domain.user.domain.UserTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserTemp, Long> {
    Optional<UserTemp> findByUsername(String username);
    Boolean existsByUsername(String username);
    Optional<UserTemp> findByRefreshToken(String refreshToken);
    Optional<UserTemp> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<UserTemp> findByEmailAndUsernameNot(String email, String username);
    Optional<UserTemp> findByGoogleId(String providerId);
    Optional<UserTemp> findByNaverId(String providerId);
    Optional<UserTemp> findByKakaoId(String providerId);


}
