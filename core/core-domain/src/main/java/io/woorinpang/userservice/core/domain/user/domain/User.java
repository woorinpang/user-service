package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.support.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class User extends BaseTimeEntity {
    @Column(name = "email", unique = true, columnDefinition = "varchar(60) not null comment '이메일'")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", columnDefinition = "varchar(15) not null comment '로그인 제공자'")
    private Provider provider;

    @Column(name = "password", columnDefinition = "varchar(120) default null comment '비밀번호'")
    private String password;

    @Column(name = "name", columnDefinition = "varchar(60) not null comment '사용자 이름'")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "userRole", columnDefinition = "varchar(15) not null comment '사용자 역할'")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "userState", columnDefinition = "varchar(15) not null comment '사용자 상태'")
    private UserState state;

    @Column(name = "lastLoginDate", columnDefinition = "datetime(6) default null comment '최근 로그인 일자'")
    private LocalDateTime lastLoginDate;

    @Column(name = "loginFailCount", columnDefinition = "tinyint default 0 comment '로그인 실패 횟수'")
    private int loginFailCount;

    public User(JoinUser user) {
        this.email = user.email();
        this.provider = user.provider();
        this.password = user.password();
        this.name = user.name();
        this.role = UserRole.ROLE_USER;
        this.state = UserState.NORMAL;
    }

    public void modify(String name) {
        this.name = name;
    }

    public void leave() {
        this.updateUserState(UserState.LEAVE);
    }

    private void updateUserState(UserState state) {
        this.state = state;
    }

    public void successLogin() {
        this.loginFailCount = 0;
        this.lastLoginDate = LocalDateTime.now();
    }

    public void failLogin() {
        this.loginFailCount++;
        if (this.loginFailCount >= 5) updateUserState(UserState.HALT);
    }
}
