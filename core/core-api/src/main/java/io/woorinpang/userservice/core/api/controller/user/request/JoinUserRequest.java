package io.woorinpang.userservice.core.api.controller.user.request;

import io.woorinpang.userservice.core.domain.user.domain.UserLogin;
import io.woorinpang.userservice.core.domain.user.domain.UserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JoinUserRequest {
    private String username;

    // (숫자)(영문)(특수문자)(공백제거)(자리수)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "{valid.password}")
    private String password;

    @Email
    private String email;

    private String name;

    public UserLogin toUserLogin(BCryptPasswordEncoder passwordEncoder) {
        return new UserLogin(this.username, passwordEncoder.encode(this.password));
    }

    public UserInfo toUserInfo() {
        return new UserInfo(this.name);
    }
}
