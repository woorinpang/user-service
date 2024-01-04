package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntity;
import io.woorinpang.userservice.core.db.user.UserQueryRepository;
import io.woorinpang.userservice.core.db.user.UserEntityRepository;
import io.woorinpang.userservice.core.db.user.dto.FindPageUserProjection;
import io.woorinpang.userservice.core.db.user.dto.UserSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFinder {
    private final UserEntityRepository userEntityRepository;
    private final UserQueryRepository userQueryRepository;

    @Transactional(readOnly = true)
    public Page<FindPageUserProjection> findPageUser(UserSearchCondition condition, Pageable pageable) {
        return userQueryRepository.findPageUser(condition, pageable);
    }

    @Transactional(readOnly = true)
    public FindUser findUser(long userId) {
        return new FindUser(UserHelper.findUserById(userEntityRepository, userId));
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findUserByUsername(String username) {
        return userEntityRepository.findByUsername(username);
    }
}
