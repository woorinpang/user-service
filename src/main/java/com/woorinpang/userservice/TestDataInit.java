package com.woorinpang.userservice;

import com.woorinpang.common.entity.Role;
import com.woorinpang.userservice.core.user.domain.User;
import com.woorinpang.userservice.core.user.domain.UserState;
import com.woorinpang.userservice.core.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final InitService initService;

    @PostConstruct
    public void init() {
        //멤버
        initService.userInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final UserRepository userRepository;
        private final BCryptPasswordEncoder passwordEncoder;

        public void userInit() {
            User user = User.createBuilder()
                    .signId(UUID.randomUUID().toString())
                    .name("spring")
                    .email("spring@naver.com")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.USER)
                    .userState(UserState.NORMAL)
                    .build();
            userRepository.save(user);
        }
    }
}
