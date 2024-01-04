package io.woorinpang.userservice.core.api.user.dto.user.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserLeaveRequest {
    @Pattern(regexp = "((?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20})|()", message = "{valid.password}") // (숫자)(영문)(특수문자)(공백제거)(자리수)
    private String password;
}
