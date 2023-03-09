package com.woorinpang.userservice.core.role.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.woorinpang.userservice.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authorizations")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authorization extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authorization_id")
    private Long id;

    @Column(name = "authorization_name", nullable = false, length = 60)
    private String name;

    @Column(nullable = false)
    private String urlPatternValue;

    @Column(nullable = false, length = 30)
    private String httpMethodCode;

    private Integer sort;

    @OneToMany(mappedBy = "authorization", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RoleAuthorization> roleAuthorizations = new ArrayList<>();

    @Builder(builderMethodName = "createAuthorization")
    public Authorization(Long id, String name, String urlPatternValue, String httpMethodCode, Integer sort, List<RoleAuthorization> roleAuthorizations) {
        this.id = id;
        this.name = name;
        this.urlPatternValue = urlPatternValue;
        this.httpMethodCode = httpMethodCode;
        this.sort = sort;
        this.roleAuthorizations = roleAuthorizations == null ? null : new ArrayList<>(roleAuthorizations);
    }

    /**
     * 인가 속성 값 수정
     */
    public void update(String authorizationName, String urlPatternValue, String httpMethodCode, Integer sort) {
        this.name = authorizationName;
        this.urlPatternValue = urlPatternValue;
        this.httpMethodCode = httpMethodCode;
        this.sort = sort;
    }
}
