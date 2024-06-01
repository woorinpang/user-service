package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserRepository;
import io.woorinpang.userservice.core.domain.user.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.woorinpang.userservice.core.domain.user.domain.UserRepositoryHelper.*;

@Component
@RequiredArgsConstructor
public class UserFinder {
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    @Transactional(readOnly = true)
    public Page<FindPageUser> findPageUser(UserSearchCondition condition, Pageable pageable) {
        return userQueryRepository.findPageUser(condition, pageable);
    }

    @Transactional(readOnly = true)
    public FindUser findUser(long userId) {
        return new FindUser(findUserById(userRepository, userId));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
