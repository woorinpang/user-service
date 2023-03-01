package com.wooringpang.userservice.core.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Embeddable
public class UserFindPasswordId implements Serializable {

    private static final long serialVersionUID = -3548302862478452070L;

    @Column(length = 60)
    private String email;

    /* 요청번호 */
    private Integer requestNo;

    @Builder
    public UserFindPasswordId(String email, Integer requestNo) {
        this.email = email;
        this.requestNo = requestNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, requestNo);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UserFindPasswordId)) return false;
        UserFindPasswordId that = (UserFindPasswordId) object;
        return Objects.equals(email, that.getEmail()) &&
                Objects.equals(requestNo, that.getRequestNo());
    }
}
