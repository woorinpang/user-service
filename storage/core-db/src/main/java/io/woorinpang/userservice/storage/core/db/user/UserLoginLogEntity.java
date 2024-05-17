package io.woorinpang.userservice.storage.core.db.user;

import io.woorinpang.userservice.storage.core.db.support.entity.BaseTimeEntity;
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
public class UserLoginLogEntity extends BaseTimeEntity {
    @Column(name = "siteId", columnDefinition = "bigint null comment '사이트 아이디'")
    private Long siteId;

    @Column(name = "username", columnDefinition = "varchar(100) not null comment '아이디'")
    private String username;

    @Column(name = "remoteIp", columnDefinition = "varchar(100) null comment '아이피'")
    private String remoteIp;

    @Column(name = "success", columnDefinition = "bit not null comment '성공여부'")
    private boolean success;

    @Column(name = "failContent", columnDefinition = "varchar(100) null comment '실패 내용'")
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
