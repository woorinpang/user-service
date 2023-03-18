package com.woorinpang.userservice.domain.log.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_login_log_id")
    private Long id;

    private Long siteId;

    @Column(length = 100)
    private String username;

    @Column(name = "reomte_it", length = 100)
    private String remoteIp;

    private Boolean isSuccess;

    @Column(name = "fail_content")
    private String failContent;

    @Builder(builderMethodName = "createUserLoginLog")
    public UserLoginLog(Long siteId, String username, String remoteIp, Boolean isSuccess, String failContent) {
        this.siteId = siteId;
        this.username = username;
        this.remoteIp = remoteIp;
        this.isSuccess = isSuccess;
        this.failContent = failContent;
    }
}
