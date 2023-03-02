package com.wooringpang.core.role.presentation;

import com.wooringpang.core.role.dto.RoleAuthorizationListDto;
import com.wooringpang.core.role.dto.RoleAuthorizationSearchCondition;
import com.wooringpang.core.role.service.RoleAuthorizationService;
import com.wooringpang.core.role.presentation.request.DeleteRoleAuthorizationRequest;
import com.wooringpang.core.role.presentation.request.SaveRoleAuthorizationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role-authorizations")
public class RoleAuthorizationController {

    private final RoleAuthorizationService roleAuthorizationService;

    /**
     * 권한 인가 페이지 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<RoleAuthorizationListDto>> findPageRoleAuthorizations(RoleAuthorizationSearchCondition condition, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roleAuthorizationService.findPageRoleAuthorizations(condition, pageable));
    }

    /**
     * 권한 인가 다건 등록
     */
    @PostMapping
    public ResponseEntity<List<RoleAuthorizationListDto>> save(@RequestBody @Validated List<SaveRoleAuthorizationRequest> requestList) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleAuthorizationService.save(requestList.stream()
                        .map(SaveRoleAuthorizationRequest::toEntity)
                        .collect(Collectors.toList()))
                );
    }

    /**
     * 권한 인가 다건 삭제
     */
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Validated List<DeleteRoleAuthorizationRequest> requestList) {
        roleAuthorizationService.delete(requestList.stream()
                .map(DeleteRoleAuthorizationRequest::toEntity)
                .collect(Collectors.toList()));
    }
}
