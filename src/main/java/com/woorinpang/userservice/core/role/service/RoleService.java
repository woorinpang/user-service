package com.woorinpang.userservice.core.role.service;

import com.woorinpang.userservice.core.role.dto.RoleListDto;
import com.woorinpang.userservice.core.role.dto.RoleSearchCondition;
import com.woorinpang.userservice.core.role.repository.RoleQueryRepository;
import com.woorinpang.userservice.core.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleQueryRepository roleQueryRepository;
    private final RoleRepository roleRepository;

    /**
     * 권한 페이지 목록 조회
     */
    public Page<RoleListDto> findPageRoles(RoleSearchCondition condition, Pageable pageable) {
        return roleQueryRepository.findPageRoles(condition, pageable);
    }

    /**
     * 권한 전체 조회
     */
    public List<RoleListDto> findAllBySort(Sort sort) {
        return roleRepository.findAll(sort).stream()
                .map(role -> RoleListDto.builder()
                        .roleCode(role.getCode())
                        .roleName(role.getName())
                        .roleContent(role.getContent())
                        .createdDate(role.getCreatedDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
