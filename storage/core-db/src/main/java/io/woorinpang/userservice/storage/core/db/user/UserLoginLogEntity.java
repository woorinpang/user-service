package io.woorinpang.userservice.storage.core.db.user;

import io.woorinpang.userservice.storage.core.db.user.dto.UserLoginLogCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserLoginLog")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginLogEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userLoginLogId")
    private Long id;

    private Long siteId;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String remoteIp;

    private boolean success;

    @Column
    private String failContent;

    @Builder(builderMethodName = "createUserLoginLog")
    public UserLoginLogEntity(UserLoginLogCommand command) {
        this.siteId = command.siteId();
        this.username = command.username();
        this.remoteIp = command.remoteIp();
        this.success = command.success();
        this.failContent = command.failContent();
    }
}
