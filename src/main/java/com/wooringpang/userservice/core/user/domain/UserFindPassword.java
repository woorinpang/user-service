package com.wooringpang.userservice.core.user.domain;

import com.wooringpang.userservice.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class UserFindPassword extends BaseEntity {

    /* 복합키 */
    @EmbeddedId
    private UserFindPasswordId userFindPasswordId;

    @Column(nullable = false, length = 50)
    private String tokenValue;
    @Column(nullable = false, columnDefinition = "tinyint(1) default '0")
    private Boolean isChange;

    /**
     *
     */
    @Builder(builderMethodName = "createUserFindPassword")
    public UserFindPassword(String email, Integer requestNo, String tokenValue, Boolean isChange) {
        this.userFindPasswordId = UserFindPasswordId.builder()
                .email(email)
                .requestNo(requestNo)
                .build();
        this.tokenValue = tokenValue;
        this.isChange = isChange;
    }

    /**
     * 변경 여부 수정
     */
    public void updateIsChange(Boolean isChange) {
        this.isChange = isChange;
    }
}
