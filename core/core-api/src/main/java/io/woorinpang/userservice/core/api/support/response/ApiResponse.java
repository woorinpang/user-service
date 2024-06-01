package io.woorinpang.userservice.core.api.support.response;

import io.woorinpang.userservice.core.api.support.error.ErrorMessage;
import io.woorinpang.userservice.core.api.support.error.ErrorType;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final ResultType result;

    private final T data;

    private final ErrorMessage error;

    private ApiResponse(ResultType result, T data, ErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(String code, String message) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(code, message));
    }

    public static ApiResponse<?> error(ErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error));
    }

    public static ApiResponse<?> error(ErrorType error, Object errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error, errorData));
    }
}
