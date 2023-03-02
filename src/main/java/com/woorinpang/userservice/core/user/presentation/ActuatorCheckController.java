package com.woorinpang.userservice.core.user.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ActuatorCheckController {

    private final Environment environment;

    /**
     * 유저 서비스 상태 확인
     */
    @GetMapping("/actuator/health-info")
    public String getStatus() {
        return """
                GET User Service on
                local.server.port : %s
                woorinpang.message : %s
                """
                .formatted(
                        environment.getProperty("local.server.port"),
                        environment.getProperty("woorinpang.message")
                );
    }

    @PostMapping("/actuator/health-info")
    public String postStatus() {
        return """
                POST User Service on
                local.server.port : %s
                woorinpang.message : %s
                """
                .formatted(
                        environment.getProperty("local.server.port"),
                        environment.getProperty("woorinpang.message")
                );
    }
}
