package io.woorinpang.userservice.storage.core.db.support.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    @Column(updatable = false, nullable = false, length = 60)
    @Comment("생성자")
    protected String createdBy;

    @LastModifiedBy
    @Column(nullable = false, length = 60)
    @Comment("수정자")
    protected String lastModifiedBy;
}
