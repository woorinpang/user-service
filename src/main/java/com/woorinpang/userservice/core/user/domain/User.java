package com.woorinpang.userservice.core.user.domain;

import com.woorinpang.servlet.entity.BaseEntity;
import com.woorinpang.userservice.core.user.application.param.UpdateUserParam;
import com.woorinpang.common.entity.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(length = 100)
    private String password;
    @Column(nullable = false, length = 60, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column
    private UserState userState;
    private String refreshToken;

    @Column
    private LocalDateTime lastLoginDate;

    @Column(nullable = false, columnDefinition = "tinyint default '0'")
    private Integer loginFailCount;

    @Column(length = 100)
    private String googleId;

    @Column(length = 100)
    private String kakaoId;

    @Column(length = 100)
    private String naverId;

    @Builder(builderMethodName = "createBuilder")
    public User(String username, String password, String email,
                String name, Role role, UserState userState,
                String googleId, String kakaoId, String naverId) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.email = email;
        this.name = name;
        this.role = role;
        this.userState = userState;
        this.googleId = googleId;
        this.kakaoId = kakaoId;
        this.naverId = naverId;
    }

    /**
     * 유저 수정
     */
    public void update(UpdateUserParam param) {
        this.email = param.getEmail();
        this.password = param.getPassword();
        this.name = param.getName();
        this.role = param.getRole();
        this.userState = param.getUserState();
    }

    /**
     * 사용자 refresh token 정보를 필드에 입력
     */
    public User updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    /**
     * 사용자 비밀번호를 필드에 입력
     */
    public User updatePassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 로그인 성공 시 로그인 실패수와 마지막 로그인 일지 정보를 갱신한다.
     */
    public void successLogin() {
        this.loginFailCount = 0;
        this.lastLoginDate = LocalDateTime.now();
    }

    /**
     * 로그인 실패 시 로그인 실패수를 증가시키고 5회 이상 실패한 경우 회원상태를 정지로 변경
     */
    public void failLogin() {
        this.loginFailCount++;
        if (this.loginFailCount >= 5) this.userState = UserState.HALT;
    }

    /**
     * 사용자 정보 수정
     */
    public void updateInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * 사용자 상태 코드 수정
     */
    public void updateUserStateCode(UserState userState) {
        this.userState = userState;
    }
}
