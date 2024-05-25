package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.domain.UserHelper.findUserById;

@Component
@RequiredArgsConstructor
public class UserRemover {
    private final UserRepository userRepository;

    @Transactional
    public void leave(long userId) {
        findUserById(userRepository, userId).leave();
    }
}
