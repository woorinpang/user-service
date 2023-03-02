package com.woorinpang.userservice.core.log.domain;

import com.woorinpang.servlet.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginLog extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_log_id")
    private Long id;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String remoteIp;

    private Boolean isSuccess;

    @Column(length = 200)
    private String failContent;

    @Builder(builderMethodName = "createLoginLog")
    public LoginLog(String email, String remoteIp, Boolean isSuccess, String failContent) {
        this.email = email;
        this.remoteIp = remoteIp;
        this.isSuccess = isSuccess;
        this.failContent = failContent;
    }
}
