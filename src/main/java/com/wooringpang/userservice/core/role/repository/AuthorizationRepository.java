package com.wooringpang.userservice.core.role.repository;

import com.wooringpang.userservice.core.role.domain.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationRepository extends JpaRepository<Authorization, Long> {

    /**
     * 정렬 순서로 인가 단건 조회
     */
    Optional<Authorization> findBySort(Integer sort);
}
