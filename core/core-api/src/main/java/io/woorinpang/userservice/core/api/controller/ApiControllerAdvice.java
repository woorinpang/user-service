package io.woorinpang.userservice.core.api.controller;

import io.woorinpang.userservice.core.api.support.error.CoreApiException;
import io.woorinpang.userservice.core.api.support.error.ApiErrorType;
import io.woorinpang.userservice.core.api.support.response.ApiResponse;
import io.woorinpang.userservice.core.domain.support.error.CoreDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(CoreApiException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreApiException(CoreApiException e) {
        switch (e.getType().getLogLevel()) {
            case ERROR -> log.error("CoreApiException : {}", e.getMessage(), e);
            case WARN -> log.warn("CoreApiException : {}", e.getMessage(), e);
            default -> log.info("CoreApiException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.error(e.getType(), e.getData()), e.getType().getStatus());
    }

    @ExceptionHandler(CoreDomainException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreDomainException(CoreDomainException e) {
        switch (e.getType().getLogLevel()) {
            case ERROR -> log.error("CoreApiException : {}", e.getMessage(), e);
            case WARN -> log.warn("CoreApiException : {}", e.getMessage(), e);
            default -> log.info("CoreApiException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.error("", ""), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(ApiErrorType.DEFAULT_ERROR), ApiErrorType.DEFAULT_ERROR.getStatus());
    }
}
