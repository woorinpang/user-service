package com.wooringpang.userservice.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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

    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default '00'")
    private String userStateCode;

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

}
