package io.woorinpang.userservice.core.db.log;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_login_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginLogEntity {
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
    public UserLoginLogEntity(Long siteId, String username, String remoteIp, Boolean isSuccess, String failContent) {
        this.siteId = siteId;
        this.username = username;
        this.remoteIp = remoteIp;
        this.isSuccess = isSuccess;
        this.failContent = failContent;
    }
}
