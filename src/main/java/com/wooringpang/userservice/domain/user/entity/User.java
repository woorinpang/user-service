package com.wooringpang.userservice.domain.user.entity;

import com.wooringpang.userservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, length = 60)
    private String username;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Column(length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default '00'")
    private UserStateCode userStateCode;

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
    public User(String userId, String username, String email, String password, Role role,
                UserStateCode userStateCode, String googleId, String kakaoId, String naverId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.userStateCode = userStateCode;
        this.googleId = googleId;
        this.kakaoId = kakaoId;
        this.naverId = naverId;
    }

    /**
     * 사용자명과 이메일 등을 수정
     */
    public User update(String username, String email, String password, Role role, UserStateCode userStateCode) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.userStateCode = userStateCode;
        return this;
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
}
