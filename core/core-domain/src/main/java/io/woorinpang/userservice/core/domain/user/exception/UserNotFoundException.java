package io.woorinpang.userservice.core.domain.user.exception;

public class UserNotFoundException extends EntityNotFoundException {

    private static final String MESSAGE = "UserId=%s은 존재하지 않습니다.";
    public UserNotFoundException(Long userId) {
        super(MESSAGE.formatted(userId));
    }
}
