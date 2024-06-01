package io.woorinpang.userservice.core.api.controller.user.request;

import io.woorinpang.userservice.core.domain.user.domain.LoginUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JoinUserRequest {
    @Email(message = "잘못된 이메일입니다.")
    private String email;

    // (숫자)(영문)(특수문자)(공백제거)(자리수)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "잘못된 비밀번호입니다.")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    public LoginUser toUserLogin(BCryptPasswordEncoder passwordEncoder) {
        return new LoginUser(this.email, passwordEncoder.encode(this.password));
    }
}
