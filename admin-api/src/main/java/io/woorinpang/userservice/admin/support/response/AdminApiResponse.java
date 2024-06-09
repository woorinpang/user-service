package io.woorinpang.userservice.admin.support.response;

import lombok.Getter;

@Getter
public class AdminApiResponse<T> {
    private final AdminResultType result;

    private final T data;

    private final AdminApiErrorMessage error;

    private AdminApiResponse(AdminResultType result, T data, AdminApiErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static AdminApiResponse<Void> success() {
        return new AdminApiResponse<>(AdminResultType.SUCCESS, null, null);
    }

    public static <T> AdminApiResponse<T> success(T data) {
        return new AdminApiResponse<>(AdminResultType.SUCCESS, data, null);
    }

    public static AdminApiResponse<?> error(String code, String message) {
        return new AdminApiResponse<>(AdminResultType.ERROR, null, new AdminApiErrorMessage(code, message));
    }

    public static AdminApiResponse<?> error(AdminApiErrorType error) {
        return new AdminApiResponse<>(AdminResultType.ERROR, null, new AdminApiErrorMessage(error));
    }

    public static AdminApiResponse<?> error(AdminApiErrorType error, Object errorData) {
        return new AdminApiResponse<>(AdminResultType.ERROR, null, new AdminApiErrorMessage(error, errorData));
    }
}
