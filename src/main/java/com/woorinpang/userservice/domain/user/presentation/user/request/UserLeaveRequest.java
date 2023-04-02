package com.woorinpang.userservice.domain.user.presentation.user.request;

import com.woorinpang.userservice.domain.user.application.dto.command.UserLeaveCommand;
import com.woorinpang.userservice.global.common.entity.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserLeaveRequest {
    @Pattern(regexp = "((?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20})|()", message = "{valid.password}") // (숫자)(영문)(특수문자)(공백제거)(자리수)
    private String password;
    @NotBlank(message = "{common.provider}{valid.required}")
    private String provider;
    private String token;

    public UserLeaveCommand toCommand() {
        return UserLeaveCommand.builder()
                .password(this.password)
                .provider(Provider.findByCode(this.provider))
                .token(this.token)
                .build();
    }
}
