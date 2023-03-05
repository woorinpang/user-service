package com.woorinpang.userservice.core.user.domain;

import com.woorinpang.servlet.entity.BaseEntity;
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
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFindPassword extends BaseEntity {

    /* 복합키 */
    @EmbeddedId
    private UserFindPasswordId userFindPasswordId;

    @Column(nullable = false, length = 50)
    private String tokenValue;
    @Column(nullable = false)
    private boolean isChange;

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
