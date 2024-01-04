package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserModifier {
    private final UserRepository userRepository;

    @Transactional
    public void modify(UserTarget target, UserInfo info) {
        userRepository.findById(target.id())
                .orElseThrow(() -> new IllegalArgumentException("없음"))
                .modify(info.toModifyUserCommand());
    }
}
