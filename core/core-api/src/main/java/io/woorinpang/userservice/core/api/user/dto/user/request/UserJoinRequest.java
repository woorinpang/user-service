package io.woorinpang.userservice.core.api.user.dto.user.request;

import io.woorinpang.userservice.core.domain.user.dto.command.UserJoinCommand;
import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.UserState;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserJoinRequest {

    private String username;
    // (숫자)(영문)(특수문자)(공백제거)(자리수)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "{valid.password}")
    private String password;
    @Email
    private String email;
    private String name;
    private String provider;
    private String token;

    public UserJoinCommand toCommand() {
        return UserJoinCommand.builder()
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .name(this.name)
                .role(Role.USER)
                .userState(UserState.NORMAL)
                .provider(this.provider)
                .token(this.token)
                .build();
    }
}
