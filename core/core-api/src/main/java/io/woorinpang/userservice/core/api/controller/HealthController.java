package io.woorinpang.userservice.core.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class HealthController {
    private final Environment env;

    @GetMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok().body("user-service");
    }

    @GetMapping("/actuator/info")
    public String status() {
        String property = env.getProperty("local.server.port");
        return """
                GET User Service on
                local.server.port : {0}
                """.replace("{0}", Objects.requireNonNull(property));
    }
}
