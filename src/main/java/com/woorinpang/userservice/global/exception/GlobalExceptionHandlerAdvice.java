package com.woorinpang.userservice.global.exception;

import com.woorinpang.userservice.global.exception.dto.ErrorResponse;
import com.woorinpang.userservice.global.exception.dto.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerAdvice {

    protected final MessageSource messageSource;

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(), messageSource);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 바인딩 객체 @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(), messageSource);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 요청은 잘 만들어졌지만, 문법 오류로 인하여 따를 수 없을때 발생한다.
     */
    @ExceptionHandler(HttpClientErrorException.UnprocessableEntity.class)
    protected ResponseEntity<ErrorResponse> handleUnprocessableEntityException(HttpClientErrorException.UnprocessableEntity e) {
        log.error("handleUnprocessableEntityException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNPROCESSABLE_ENTITY, messageSource);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 지원하지 않은 Http method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED, messageSource);
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(response);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @requestParam enum 으로 binding 못했을 때 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e, messageSource);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 요청한 페이지가 존재하지 않는 경우
     */
    /*@ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error("handleNotFoundException", e);
        final ErrorResponse response = ErrorResponse.of(e, messageSource);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }*/

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ACCESS_DENIED, messageSource);
        return ResponseEntity
                .status(HttpStatus.valueOf(ErrorCode.ACCESS_DENIED.getStatus()))
                .body(response);
    }

    /**
     * 사용자 인증되지 않은 경우 발생
     */
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    protected ResponseEntity<ErrorResponse> handleUnauthorizedException(HttpClientErrorException.Unauthorized e) {
        log.error("handleUnauthorizedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED, messageSource);
        return ResponseEntity
                .status(HttpStatus.valueOf(ErrorCode.ACCESS_DENIED.getStatus()))
                .body(response);
    }

    /**
     * JWT 인증 만료 경우 발생
     */
    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("handleExpiredJwtException", e);
        final ErrorResponse reponse = ErrorResponse.of(ErrorCode.JWT_EXPIRED, messageSource);
        return ResponseEntity
                .status(HttpStatus.valueOf(ErrorCode.JWT_EXPIRED.getStatus()))
                .body(reponse);
    }

    /**
     * 사용자에게 표시할 다양한 메시지를 직접 정의하여 처리하는 Business RuntimeException Handler
     * 개발자가 만들어 던지는 런타임 오류를 처리
     */
    @ExceptionHandler(BusinessMessageException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessMessageException(BusinessMessageException e) {
        log.error("handleBusinessMessageException", e);
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode(), e.getCustomMessage());
        return ResponseEntity
                .status(HttpStatus.valueOf(e.getErrorCode().getStatus()))
                .body(response);
    }

    /**
     * 개발자가 정의 ErrorCode 를 처리하는 Business RuntimeException Handler
     * 개발자가 만들어 던지는 런타임 오류를 처리
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode(), messageSource);
        return ResponseEntity
                .status(HttpStatus.valueOf(e.getErrorCode().getStatus()))
                .body(response);
    }

    /**
     * default exception
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, messageSource);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
