package io.woorinpang.userservice.storage.db.core.user;

import io.woorinpang.userservice.storage.db.core.AdminBaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUser extends AdminBaseTimeEntity {
    @Column(name = "email", unique = true, columnDefinition = "varchar(60) not null comment '이메일'")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", columnDefinition = "varchar(15) not null comment '로그인 제공자'")
    private AdminProvider provider;

    @Column(name = "password", columnDefinition = "varchar(120) default null comment '비밀번호'")
    private String password;

    @Column(name = "name", columnDefinition = "varchar(60) not null comment '사용자 이름'")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "userRole", columnDefinition = "varchar(15) not null comment '사용자 역할'")
    private AdminUserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "userState", columnDefinition = "varchar(15) not null comment '사용자 상태'")
    private AdminUserState state;

    @Column(name = "lastLoginDate", columnDefinition = "datetime(6) default null comment '최근 로그인 일자'")
    private LocalDateTime lastLoginDate;

    @Column(name = "loginFailCount", columnDefinition = "tinyint default 0 not null comment '로그인 실패 횟수'")
    private int loginFailCount;
}
