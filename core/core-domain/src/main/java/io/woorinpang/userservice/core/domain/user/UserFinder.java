package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.User;
import io.woorinpang.userservice.core.db.user.UserQueryRepository;
import io.woorinpang.userservice.core.db.user.UserRepository;
import io.woorinpang.userservice.core.db.user.dto.FindPageUserProjection;
import io.woorinpang.userservice.core.db.user.dto.UserSearchCondition;
import io.woorinpang.userservice.core.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        return new FindUser(
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException(String.valueOf(userId)))
        );
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
