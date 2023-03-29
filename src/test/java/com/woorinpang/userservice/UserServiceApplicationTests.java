package com.woorinpang.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@EnableAutoConfiguration(exclude = {
        TestDataInit.class
})
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
