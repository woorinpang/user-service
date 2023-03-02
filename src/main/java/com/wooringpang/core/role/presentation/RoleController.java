package com.wooringpang.core.role.presentation;

import com.wooringpang.core.role.dto.RoleListDto;
import com.wooringpang.core.role.dto.RoleSearchCondition;
import com.wooringpang.core.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    /**
     * 권한 페이지 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<RoleListDto>> findPageRoles(
            RoleSearchCondition condition,
            @PageableDefault(sort = "sort", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleService.findPageRoles(condition, pageable));
    }

    /**
     * 권한 정렬 순서 오름차순 전체 목록 조회
     */
    @GetMapping("/all")
    public ResponseEntity<List<RoleListDto>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleService.findAllBySort(Sort.by(Sort.Direction.ASC, "sort")));
    }
}
