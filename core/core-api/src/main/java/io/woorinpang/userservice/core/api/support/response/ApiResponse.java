package io.woorinpang.userservice.core.api.support.response;

import io.woorinpang.userservice.core.api.support.error.ApiErrorMessage;
import io.woorinpang.userservice.core.api.support.error.ApiErrorType;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final ResultType result;

    private final T data;

    private final ApiErrorMessage error;

    private ApiResponse(ResultType result, T data, ApiErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(String code, String message) {
        return new ApiResponse<>(ResultType.ERROR, null, new ApiErrorMessage(code, message));
    }

    public static ApiResponse<?> error(ApiErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ApiErrorMessage(error));
    }

    public static ApiResponse<?> error(ApiErrorType error, Object errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ApiErrorMessage(error, errorData));
    }
}
