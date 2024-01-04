package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.woorinpang.userservice.core.domain.user.UserHelper.findUserById;

@Component
@RequiredArgsConstructor
public class UserRemover {
    private final UserEntityRepository userEntityRepository;

    public void leave(long userId) {
        findUserById(userEntityRepository, userId)
                .leave();
    }
}
