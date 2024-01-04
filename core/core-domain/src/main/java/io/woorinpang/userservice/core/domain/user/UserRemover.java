package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRemover {
    private final UserRepository userRepository;

    public void leave(long userId) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없음"))
                .leave();
    }
}
