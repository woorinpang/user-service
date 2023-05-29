package com.woorinpang.userservice.global.common.json;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.springframework.util.StringUtils.hasText;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JsonResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String code;
    private String message;

    private T data;

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    @Builder
    private JsonResponse(LocalDateTime timestamp, HttpStatus httpStatus, String message, T data) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.code = httpStatus.getReasonPhrase();
        this.message = hasText(message) ? message : DEFAULT_SUCCESS_MESSAGE;
        this.data = data;
    }

    /**
     * 객체 없이 반환
     */
    public static <T> JsonResponse<T> OK() {
        return (JsonResponse<T>) JsonResponse.builder()
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * 객체와 반환
     */
    public static <T> JsonResponse<T> OK(T data) {
        return (JsonResponse<T>) JsonResponse.builder()
                .httpStatus(HttpStatus.OK)
                .data(data)
                .build();
    }

    /**
     * 객체와 메시지 반환
     */
    public static <T> JsonResponse<T> OK(String message, T data) {
        return (JsonResponse<T>) JsonResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 생성시 객체 없이 반환
     */
    public static <T> JsonResponse<T> CREATED() {
        return (JsonResponse<T>) JsonResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    /**
     * 생성시 겍체 리턴
     */
    public static <T> JsonResponse<T> CREATED(T data) {
        return (JsonResponse<T>) JsonResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .data(data)
                .build();
    }

    /**
     * 생성시 객체와 메시지 리턴
     */
    public static <T> JsonResponse<T> CREATED(String message, T data) {
        return (JsonResponse<T>) JsonResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 삭제시 리턴
     */
    public static <T> JsonResponse<T> NO_CONTENT() {
        return (JsonResponse<T>) JsonResponse.builder()
                .httpStatus(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * 삭제시 메시지와 리턴
     */
    public static <T> JsonResponse<T> NO_CONTENT(String message) {
        return (JsonResponse<T>) JsonResponse.builder()
                .httpStatus(HttpStatus.NO_CONTENT)
                .message(message)
                .build();
    }
}
