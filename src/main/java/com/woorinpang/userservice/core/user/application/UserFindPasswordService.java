package com.woorinpang.userservice.core.user.application;

import com.woorinpang.userservice.core.user.domain.User;
import com.woorinpang.userservice.core.user.domain.UserFindPassword;
import com.woorinpang.userservice.core.user.domain.UserFindPasswordRepository;
import com.woorinpang.userservice.core.user.domain.UserRepository;
import com.woorinpang.userservice.core.user.infrastructure.UserFindPasswordQueryRepository;
import com.woorinpang.userservice.core.user.presentation.request.UserFindPasswordSaveRequest;
import com.woorinpang.userservice.core.user.presentation.request.UserFindPasswordUpdateRequest;
import com.woorinpang.userservice.core.user.presentation.request.UserPasswordUpdateRequest;
import com.woorinpang.userservice.global.config.UserPasswordChangeEmail;
import com.woorinpang.userservice.global.exception.BusinessMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFindPasswordService {

    private final UserFindPasswordQueryRepository userFindPasswordQueryRepository;
    private final UserFindPasswordRepository userFindPasswordRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
//    private final JavaMailSender javaMailSender;

    private final UserService userService;

    /**
     * 사용자 비밀번호 찾기
     */
    public Boolean findPassword(UserFindPasswordSaveRequest request) {
        String email = request.getEmail();

        User findUser = userRepository.findByEmailAndUsername(email, request.getUserName())
                .orElseThrow(() -> new BusinessMessageException("없음"));

        //이메일 전송
        try {
            final String mainUrl = request.getMainUrl();
            final String tokenValue = UUID.randomUUID().toString().replaceAll("-", "");

            final String subjec = "이메일 유저 패스워드 타이틀";
            final String text = UserPasswordChangeEmail.html;
            final String username = findUser.getUsername();
            final String changePasswordUrl = request.getChangePasswordUrl() + "?token=" + tokenValue;

//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message);
//
//            helper.setTo(email);
//            helper.setSubject(subjec);
//            helper.setText(String.format(text, mainUrl, username, changePasswordUrl), true); //String.format 에서 %를 쓰려면 %%로
//
//            log.info("start send change password email: " + email);
//            javaMailSender.send(message);
//
//            Integer requestNo = userFindPasswordQueryRepository.findNextRequestNo(email);
//            UserFindPassword userFindPassword = request.toEntity(requestNo, tokenValue);
//
//            userFindPasswordRepository.save(userFindPassword);
//
//            log.info("end send change password email - email: {}, tokenValue: {}", email, tokenValue);
//        } catch (MessagingException e) {
//            String errorMessage = "error.user.find.password";
//            log.error(errorMessage + ": " + e.getMessage());
//            throw new BusinessMessageException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "error.user.find.password";
            log.error(errorMessage + ": " + e.getMessage());
            throw new BusinessMessageException(errorMessage);
        }
        return true;
    }

    /**
     * 사용자 비밀번호 찾기 유효성 확인
     */
    @Transactional
    public Boolean validPassword(String tokenValue) {
        if (!hasText(tokenValue)) {
            throw new IllegalArgumentException("invalid");
        }

        Optional<UserFindPassword> findUserFindPassword = userFindPasswordRepository.findByTokenValue(tokenValue);
        if (findUserFindPassword.isPresent()) {
            UserFindPassword userFindPassword = findUserFindPassword.get();

            boolean isExpired = LocalDateTime.now().isAfter(userFindPassword.getCreatedDate().plusHours(1));//1시간 후 만료
            if(Boolean.FALSE.equals(userFindPassword.isChange()) && !isExpired) return true;
        }
        return false;
    }

    /**
     * 사용자 비밀번호 찾기 변경
     */
    @Transactional
    public Boolean changePassword(UserFindPasswordUpdateRequest request) {
        final String tokenValue = request.getTokenValue();

        UserFindPassword findUserFindPassword = userFindPasswordRepository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new BusinessMessageException("없음"));

        if (Boolean.TRUE.equals(findUserFindPassword.isChange()) || LocalDateTime.now().isAfter(findUserFindPassword.getCreatedDate().plusHours(1))) {
            throw new BusinessMessageException("에러");
        }

        User findUser = userRepository.findByEmail(findUserFindPassword.getUserFindPasswordId().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("없음"));

        findUser.updatePassword(passwordEncoder.encode(request.getPassword()));

        findUserFindPassword.updateIsChange(Boolean.TRUE);

        return true;
    }

    /**
     * 사용자 비밀번호 변경
     */
    @Transactional
    public Boolean updatePassword(String username, UserPasswordUpdateRequest request) {
        try {
            User user = userService.findUserVerify(username, request);

            user.updatePassword(passwordEncoder.encode(request.getPassword()));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw e;
        }
        return true;
    }

    /**
     * 사용자 비밀번호 확인
     */
    public Boolean matchPassword(String username, String password) {
        try {
            userService.findUserVerifyPassword(username, password);
        } catch (BusinessMessageException e) {
            return false;
        }
        return true;
    }
}
