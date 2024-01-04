package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.UserHelper.*;

@Component
@RequiredArgsConstructor
public class UserModifier {
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void modify(UserTarget target, UserInfo info) {
        findUserById(userEntityRepository, target.id())
                .modify(info.toModifyUserCommand());
    }
}
