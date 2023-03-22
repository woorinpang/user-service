package com.woorinpang.userservice;

import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.global.config.TokenProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        private final TokenProvider tokenProvider;
        public void userInit() {
            User user = User.createBuilder()
                    .username("spring")
                    .email("spring@naver.com")
                    .password("1234")
                    .name("스프링")
                    .role(Role.ADMIN)
                    .userState(UserState.NORMAL)
                    .build();
            userRepository.save(user);
        }
    }
}
