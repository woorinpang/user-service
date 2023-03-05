package com.woorinpang.userservice.core.user.repository;

import com.woorinpang.userservice.core.user.UserTestConfig;
import com.woorinpang.userservice.core.user.domain.User;
import com.woorinpang.userservice.test.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(UserTestConfig.class)
class UserQueryRepositoryTest extends RepositoryTest {

    @Autowired protected UserQueryRepository userQueryRepository;

    @Test
    void 회원_목록_조회() {
        //given
        User user = User.createBuilder()
                .build();

        //when

        //then
    }

}