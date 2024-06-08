package io.woorinpang.userservice.core.domain.user.domain;

import io.woorinpang.userservice.core.domain.support.error.CoreDomainException;
import io.woorinpang.userservice.core.domain.support.error.DomainErrorType;
import io.woorinpang.userservice.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.woorinpang.userservice.core.domain.user.domain.UserRepositoryHelper.*;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public boolean existsEmail(String email) {
        return existsByEmail(userRepository, email);
    }

    @Transactional(readOnly = true)
    public void validEmail(String email) {
        if (!existsByEmail(userRepository, email)) return;

        throw new CoreDomainException(DomainErrorType.ALREADY_EXISTS_EMAIL);
    }

    @Transactional(readOnly = true)
    public void validEmailAndProvider(UserEmailWithProvider user) {
        if (!userRepository.existsByEmailAndProviderIsNot(user.email(), user.provider())) return;

        throw new CoreDomainException(DomainErrorType.ALREADY_EXISTS_EMAIL_OTHER_PROVIDER);
    }
}
