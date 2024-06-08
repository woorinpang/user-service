package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.domain.UserRepositoryHelper.saveUser;

@Component
@RequiredArgsConstructor
public class UserAppender {
    private final UserRepository userRepository;

    @Transactional
    public long append(LoginUser login, String name, Provider provider) {
        JoinUser command = JoinUser.builder()
                .email(login.email())
                .provider(provider)
                .password(login.password())
                .name(name)
                .build();

        return saveUser(userRepository, new User(command)).getId();
    }
}
