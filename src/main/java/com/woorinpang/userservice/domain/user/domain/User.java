package com.woorinpang.userservice.domain.user.domain;

import com.woorinpang.userservice.domain.user.application.dto.command.UpdateUserCommand;
import com.woorinpang.userservice.domain.user.application.dto.command.UserUpdateInfoCommand;
import com.woorinpang.userservice.global.common.entity.BaseEntity;
import com.woorinpang.userservice.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.springframework.util.StringUtils.*;

@Entity
@Table(name = "users")
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", unique = true, columnDefinition = "varchar(60) not null comment '아이디'")
    private String username;

    @Column(name = "password", columnDefinition = "varchar(120) default null comment '비밀번호'")
    private String password;

    @Column(name = "email", unique = true, columnDefinition = "varchar(60) not null comment '이메일'")
    private String email;

    @Column(name = "name", columnDefinition = "varchar(60) not null comment '사용자 이름'")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(15) not null comment '사용자 역할'")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "userState", columnDefinition = "varchar(15) not null comment '사용자 상태'")
    private UserState userState;

    @Column(name = "refreshToken", columnDefinition = "varchar(255) default null comment '리프레시 토큰'")
    private String refreshToken;

    @Column(name = "lastLoginDate", columnDefinition = "datetime(6) default null comment '최근 로그인 일자'")
    private LocalDateTime lastLoginDate;

    @Column(name = "loginFailCount", columnDefinition = "tinyint default 0 comment '로그인 실패 횟수'")
    private int loginFailCount;

    @Column(name = "googleId", columnDefinition = "varchar(60) default null comment '구글 아이디'")
    private String googleId;

    @Column(name = "kakaoId", columnDefinition = "varchar(60) default null comment '카카오 아이디'")
    private String kakaoId;

    @Column(name = "naverId", columnDefinition = "varchar(60) default null comment '네이버 아이디'")
    private String naverId;

    /**
     * 사용자 생성
     */
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
     * 사용자 수정
     */
    public void update(UpdateUserCommand command) {
        //새로운 비밀번호가 들어오면 인코드 아니면 기존 비밀번호 업데이트
        this.password = hasText(command.password()) ? new BCryptPasswordEncoder().encode(command.password()) : this.password;
        this.email = command.email();
        this.name = command.name();
        this.role = command.role();
        this.userState = command.userState();
    }

    /**
     * 사용자 상태 코드 수정
     */
    public void updateUserStateCode(UserState userState) {
        this.userState = userState;
    }

    /**
     * 사용자 정보 수정
     */
    public void updateInfo(UserUpdateInfoCommand command) {
        this.name = command.name();
        this.email = command.email();
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
     * 소셜 사용자 여부 반환
     */
    public boolean isSocialUser() {
        if (hasText(this.googleId)) return true;
        else if (hasText(this.kakaoId)) return true;
        else if (hasText(this.name)) return true;
        return false;
    }
}
