package com.wooringpang.core.role.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @Column(name = "roleCode", nullable = false, length = 20, unique = true)
    private String code;

    @Column(name = "role_name", nullable = false, length = 50)
    private String name;

    @Column(name = "role_content")
    private String content;

    @Column
    private Integer sort;

    @CreatedDate
    @Column
    private LocalDateTime createdDate;

    @Builder(builderMethodName = "createRole")
    public Role(String code, String name, String content, Integer sort) {
        this.code = code;
        this.name = name;
        this.content = content;
        this.sort = sort;
    }
}
