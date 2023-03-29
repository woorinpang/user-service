package com.woorinpang.userservice.global.common.json;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JsonResponse<T> {

    private LocalDateTime timestamp;
    private String message;
    private int status;
    private String code;

    private T data;

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    @Builder
    private JsonResponse(LocalDateTime timestamp, HttpStatus httpStatus, T data) {
        this.timestamp = timestamp;
        this.message = httpStatus.getReasonPhrase();
        this.status = httpStatus.value();
        this.code = DEFAULT_SUCCESS_MESSAGE;
        this.data = data;
    }

    /**
     * 객체 없이 반환시
     */
    public static <T> JsonResponse<T> OK() {
        return (JsonResponse<T>) JsonResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * 객체와 반환시
     */
    public static <T> JsonResponse<T> OK(T data) {
        return (JsonResponse<T>) JsonResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(HttpStatus.OK)
                .data(data)
                .build();
    }

    /**
     * 생성시 리턴
     */
    public static <T> JsonResponse<T> CREATED(T data) {
        return (JsonResponse<T>) JsonResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(HttpStatus.CREATED)
                .data(data)
                .build();
    }

    /**
     * 삭제시 리턴
     */
    public static <T> JsonResponse<T> NO_CONTENT() {
        return (JsonResponse<T>) JsonResponse.builder()
                .timestamp(LocalDateTime.now())
                .httpStatus(HttpStatus.NO_CONTENT)
                .build();
    }
}
