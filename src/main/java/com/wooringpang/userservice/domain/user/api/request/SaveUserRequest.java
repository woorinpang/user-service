package com.wooringpang.userservice.domain.user.api.request;

import com.wooringpang.userservice.domain.user.entity.Role;
import com.wooringpang.userservice.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveUserRequest {

    private String username;
    private String email;
    private String password;
    private String roleId;
    private String userStateCode;

    @Builder
    public SaveUserRequest(String username, String email, String password, String roleId, String userStateCode) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.userStateCode = userStateCode;
    }

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.createBuilder()
                .username(this.username)
                .email(this.email)
                .password(passwordEncoder.encode(password)) //패스워드 인코딩
                .userId(UUID.randomUUID().toString()) //사용자 아이디 랜덤하게 생성
                .role(Arrays.stream(Role.values()).filter(role -> role.getCode().equals(this.roleId)).findAny().orElse(null))
                .userStateCode(this.userStateCode)
                .build();
    }
}
