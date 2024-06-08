package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.domain.UserRepositoryHelper.findUserById;

@Component
@RequiredArgsConstructor
public class UserRemover {
    private final UserRepository userRepository;

    @Transactional
    public void leave(UserTarget target) {
        findUserById(userRepository, target.id()).leave();
    }
}
