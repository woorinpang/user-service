package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.support.error.CoreDomainException;
import io.woorinpang.userservice.core.domain.support.error.DomainErrorType;
import io.woorinpang.userservice.core.domain.user.repository.UserQueryRepository;
import io.woorinpang.userservice.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.woorinpang.userservice.core.domain.user.domain.UserRepositoryHelper.findUserById;

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
    public FindUser findUser(UserTarget target) {
        return new FindUser(findUserById(userRepository, target.id()));
    }

    @Transactional(readOnly = true)
    public FindUser findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(FindUser::new)
                .orElseThrow(() -> new CoreDomainException(DomainErrorType.USER_ENTITY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<User> findProviderUser(UserEmailWithProvider user) {
        return userRepository.findByEmailAndProvider(user.email(), user.provider());
    }
}
