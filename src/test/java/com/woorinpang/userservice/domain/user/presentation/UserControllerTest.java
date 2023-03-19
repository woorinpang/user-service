package com.woorinpang.userservice.domain.user.presentation;

import com.woorinpang.userservice.domain.user.application.UserService;
import com.woorinpang.userservice.test.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserControllerTest extends IntegrationTest {

    @Autowired protected UserService userService;
    @Test
    void test() {

    }
}
