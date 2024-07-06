package io.woorinpang.userservice.storage.db.core.user;

import io.woorinpang.userservice.core.domain.user.domain.UserLoginLogCommand;
import io.woorinpang.userservice.storage.db.core.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserLoginLog")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginLog extends BaseTimeEntity {
    @Column(name = "siteId", columnDefinition = "bigint null comment '사이트 아이디'")
    private Long siteId;

    @Column(name = "email", columnDefinition = "varchar(100) not null comment '이메일'")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", columnDefinition = "varchar(15) not null comment '로그인 제공자'")
    private Provider provider;

    @Column(name = "remoteIp", columnDefinition = "varchar(100) null comment '아이피'")
    private String remoteIp;

    @Column(name = "success", columnDefinition = "bit not null comment '성공여부'")
    private boolean success;

    @Column(name = "failContent", columnDefinition = "varchar(100) null comment '실패 내용'")
    private String failContent;

    public UserLoginLog(UserLoginLogCommand log) {
        this.siteId = log.siteId();
        this.email = log.email();
        this.provider = log.provider();
        this.remoteIp = log.remoteIp();
        this.success = log.success();
        this.failContent = log.failContent();
    }
}
