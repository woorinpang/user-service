package io.woorinpang.userservice.core.domain.user;

import io.woorinpang.userservice.storage.core.db.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void duplicateLoginId(String username) {
        if (userRepository.existsByUsername(username)) throw new IllegalArgumentException("동일한 로그인 아이디가 존재합니다.");
    }

    public void duplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) throw new IllegalArgumentException("동일한 이메일이 존재합니다.");
    }
}
