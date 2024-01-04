package io.woorinpang.userservice.core.db.user;

import io.woorinpang.userservice.core.db.user.dto.ModifyUserCommand;
import io.woorinpang.userservice.core.db.user.dto.UserJoinCommand;
import io.woorinpang.userservice.core.enums.user.UserRole;
import io.woorinpang.userservice.core.enums.user.UserState;
import io.woorinpang.userservice.core.support.entity.BaseTimeEntity;
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
public class UserEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
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
    @Column(name = "userRole", columnDefinition = "varchar(15) not null comment '사용자 역할'")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "userState", columnDefinition = "varchar(15) not null comment '사용자 상태'")
    private UserState state;

    @Column(name = "refreshToken", columnDefinition = "varchar(255) default null comment '리프레시 토큰'")
    private String refreshToken;

    @Column(name = "lastLoginDate", columnDefinition = "datetime(6) default null comment '최근 로그인 일자'")
    private LocalDateTime lastLoginDate;

    @Column(name = "loginFailCount", columnDefinition = "tinyint default 0 comment '로그인 실패 횟수'")
    private int loginFailCount;

    public UserEntity(UserJoinCommand command) {
        this.username = command.username();
        this.password = command.password();
        this.email = command.email();
        this.name = command.name();
        this.role = UserRole.USER;
        this.state = UserState.NORMAL;
    }

    /**
     * 사용자 수정
     */
    public void modify(ModifyUserCommand command) {
        this.email = command.email();
        this.name = command.name();
    }

    public void leave() {
        this.updateUserState(UserState.LEAVE);
    }

    /**
     * 사용자 상태 코드 수정
     */
    private void updateUserState(UserState state) {
        this.state = state;
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
        if (this.loginFailCount >= 5) this.state = UserState.HALT;
    }

    public void modifyRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
