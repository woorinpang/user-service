package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void duplicateLoginId(String email) {
        if (userRepository.existsByEmail(email)) throw new IllegalArgumentException("동일한 로그인 아이디가 존재합니다.");
    }

    public void duplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) throw new IllegalArgumentException("동일한 이메일이 존재합니다.");
    }
}
