package io.woorinpang.userservice.core.db.user;

import io.woorinpang.userservice.core.support.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(60) not null comment '사용자 이름'")
    private String name;

    @Column(name = "email", unique = true, columnDefinition = "varchar(60) not null comment '이메일'")
    private String email;

    @Column(name = "password", columnDefinition = "varchar(120) default null comment '비밀번호'")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(15) not null comment '사용자 역할'")
    private Role role;
}
