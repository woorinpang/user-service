package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntity;
import io.woorinpang.userservice.core.db.user.UserQueryRepository;
import io.woorinpang.userservice.core.db.user.UserRepository;
import io.woorinpang.userservice.core.db.user.dto.FindPageUserProjection;
import io.woorinpang.userservice.core.db.user.dto.UserSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.woorinpang.userservice.core.domain.user.UserHelper.*;

@Component
@RequiredArgsConstructor
public class UserFinder {
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    @Transactional(readOnly = true)
    public Page<FindPageUserProjection> findPageUser(UserSearchCondition condition, Pageable pageable) {
        return userQueryRepository.findPageUser(condition, pageable);
    }

    @Transactional(readOnly = true)
    public FindUser findUser(long userId) {
        return new FindUser(findUserById(userRepository, userId));
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByRefreshToken(String refreshToken) {
         return userRepository.findByRefreshToken(refreshToken);
    }
}
