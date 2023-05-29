package com.woorinpang.userservice.test;

import com.woorinpang.userservice.domain.user.application.dto.UserCommandMapper;
import com.woorinpang.userservice.domain.user.infrastructure.UserQueryRepository;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UnitTest {
    //인코더
    @Mock protected BCryptPasswordEncoder passwordEncoder;

    //맵스트럭
    @Mock protected UserCommandMapper userCommandMapper;

    //사용자
    @Mock protected UserQueryRepository userQueryRepository;
    @Mock protected UserRepository userRepository;
}
