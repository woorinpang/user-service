package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.core.db.user.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserEntityRepository userEntityRepository;

    public void duplicateLoginId(String username) {
        if (userEntityRepository.existsByUsername(username)) throw new IllegalArgumentException("동일한 로그인 아이디가 존재합니다.");
    }

    public void duplicateEmail(String email) {
        if (userEntityRepository.existsByEmail(email)) throw new IllegalArgumentException("동일한 이메일이 존재합니다.");
    }
}
