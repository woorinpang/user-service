package io.woorinpang.userservice.support.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String code;
    private int status;
    private String message;
    private List<FieldError> errors;

    private static final String DEFAULT_ERROR_MESSAGE = "ERROR";

    private ErrorResponse(final ErrorCode code, final List<FieldError> errors, MessageSource messageSource) {
        this.timestamp = LocalDateTime.now();
        this.message = messageSource.getMessage(code.getMessage(), new Object[]{}, DEFAULT_ERROR_MESSAGE, LocaleContextHolder.getLocale());
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>(errors);
    }

    private ErrorResponse(final ErrorCode code, MessageSource messageSource) {
        this.timestamp = LocalDateTime.now();
        this.message = messageSource.getMessage(code.getMessage(), new Object[]{}, DEFAULT_ERROR_MESSAGE, LocaleContextHolder.getLocale());
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(final ErrorCode code, String customMessage) {
        this.timestamp = LocalDateTime.now();
        this.message = customMessage;
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }

    /**
     * 사용자 정의 메시지를 받아 넘기는 경우
     */
    public static ErrorResponse of(final ErrorCode code, final String customMessage) {
        return new ErrorResponse(code, customMessage);
    }

    /**
     * @Valid 어노테이션으로 넘어오는 값의 경우
     */
    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult, MessageSource messageSource) {
        return new ErrorResponse(code, FieldError.of(bindingResult), messageSource);
    }

    /**
     *
     */
    public static ErrorResponse of(final ErrorCode code, MessageSource messageSource) {
        return new ErrorResponse(code, messageSource);
    }

    /**
     *
     */
    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors, MessageSource messageSource) {
        return new ErrorResponse(code, errors, messageSource);
    }

    /**
     * java validator 에러 발생 시 에러 정보 중 필요한 내용만 FieldError 로 반환한다.
     */
    public static ErrorResponse of(MethodArgumentTypeMismatchException e, MessageSource messageSource) {
        if (e.getValue() == null) {
            return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, FieldError.of(e.getName(), "", e.getErrorCode()), messageSource);
        }

        final List<FieldError> errors = FieldError.of(e.getName(), String.valueOf(e.getValue()), e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors, messageSource);
    }
    /**
     * java validator 에러 발생시 에러 정보 중 필요한 내용만 반환한다.
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class FieldError {
        private String field;
        private String rejectedValue;
        private String message;

        private FieldError(final String field, final String rejectedValue, final String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public static List<FieldError> of(final String field, final String rejectedValue, final String message) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, rejectedValue, message));
            return fieldErrors;
        }

        /**
         * BindingResult to FieldError
         */
        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    ))
                    .collect(Collectors.toList());
        }
    }
}
