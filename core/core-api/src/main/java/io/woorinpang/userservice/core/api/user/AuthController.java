package io.woorinpang.userservice.core.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/signup")
@RequiredArgsConstructor
public class AuthController {
    @PostMapping
    public void signup(
            @RequestBody SignupRequest request
    ) {

    }
}
