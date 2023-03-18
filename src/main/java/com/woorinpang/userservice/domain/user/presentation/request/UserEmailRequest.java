package com.woorinpang.userservice.domain.user.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmailRequest {

    private String username;

    @NotBlank(message = "{user.email}{valid.required}")
    @Email
    private String email;
}
