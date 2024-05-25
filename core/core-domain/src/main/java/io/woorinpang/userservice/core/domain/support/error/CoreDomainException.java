package io.woorinpang.userservice.core.domain.support.error;

import lombok.Getter;

@Getter
public class CoreDomainException extends RuntimeException{
    private final DomainErrorType type;

    public CoreDomainException(DomainErrorType type) {
        super(type.getMessage());
        this.type = type;
    }
}
