package com.woorinpang.userservice.domain.user.application;

import com.woorinpang.userservice.domain.user.application.dto.UserCommandMapper;
import com.woorinpang.userservice.domain.user.application.dto.command.UserJoinCommand;
import com.woorinpang.userservice.domain.user.application.dto.command.UserUpdateInfoCommand;
import com.woorinpang.userservice.domain.user.exception.PasswordNotMatchException;
import com.woorinpang.userservice.domain.user.infrastructure.dto.UserSearchCondition;
import com.woorinpang.userservice.domain.user.application.dto.command.SaveUserCommand;
import com.woorinpang.userservice.domain.user.application.dto.command.UpdateUserCommand;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.exception.EmailAlreadyExistsException;
import com.woorinpang.userservice.domain.user.exception.UserNotFoundException;
import com.woorinpang.userservice.domain.user.exception.UsernameAlreadyExistsException;
import com.woorinpang.userservice.domain.user.infrastructure.UserQueryRepository;
import com.woorinpang.userservice.domain.user.infrastructure.UserRepository;
import com.woorinpang.userservice.domain.user.infrastructure.dto.FindPageUserDto;
import com.woorinpang.userservice.test.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.woorinpang.userservice.domain.user.UserSetup.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("UserService 단위 테스트")
class UserServiceTest extends UnitTest {

    @InjectMocks private UserService userService;
    @Mock private UserQueryRepository userQueryRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserCommandMapper userCommandMapper;
    @Mock private BCryptPasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("사용자_목록_조회하면_")
    class FindUsers {
        @Test
        @DisplayName("10건 조회한다.")
        void test01() {
            //given
            given(userQueryRepository.findPageUsers(any(UserSearchCondition.class), any(Pageable.class)))
                    .willReturn(new PageImpl<>(getFindPageUserDtos()));

            UserSearchCondition condition = UserSearchCondition.builder().build();
            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<FindPageUserDto> content = userService.findUsers(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(10);
            assertThat(content.getContent().size()).isEqualTo(10);
            assertThat(content.getTotalPages()).isEqualTo(1);

            //verify
            verify(userQueryRepository, times(1)).findPageUsers(any(UserSearchCondition.class), any(PageRequest.class));
        }
    }

    @Nested
    @DisplayName("사용자_단건_조회하면_")
    class FindUser {
        @Test
        @DisplayName("1건 조회된다.")
        void test01() {
            //given
            User user = getUser();
            given_optional_of_nullable_user(user);

            //when
            User findUser = userService.findUser(USER_ID);

            //then
            assertThat(findUser.getUsername()).isEqualTo(USERNAME);
            assertThat(findUser.getPassword()).isNotEqualTo(PASSWORD);
            assertThat(findUser.getEmail()).isEqualTo(EMAIL);
            assertThat(findUser.getName()).isEqualTo(NAME);
            assertThat(findUser.getRole()).isEqualTo(ROLE);
            assertThat(findUser.getUserState()).isEqualTo(USER_STATE);

            //verify
            verify(userRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("userId = 0L 으로 조회실패하고 UserNotFoundException 이 발생한다.")
        void test02() {
            //given
            given_user_not_found_exception();

            //expected
            assertThatThrownBy(() -> userService.findUser(USER_NOT_FOUND_ID))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage(USER_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("사용자_저장하면_")
    class SaveUser {
        @Test
        @DisplayName("SaveUserCommand 객체를 받아 저장한다.")
        void test01() {
            //given
            User user = getUser();
            given(userRepository.save(any(User.class))).willReturn(user);
            given(userCommandMapper.toUser(any(SaveUserCommand.class))).willReturn(user);
            SaveUserCommand command = getSaveUserCommand();

            //when
            Long savedUserId = userService.saveUser(command);

            //then

            //verify
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("Username 중복시 UsernameAlreadyExistsException 이 발생한다")
        void test02() {
            //given
            given_username_already_exists_exception();

            //expected
            assertThatThrownBy(() -> userService.saveUser(getSaveUserCommand()))
                    .isInstanceOf(UsernameAlreadyExistsException.class)
                    .hasMessage("Username=%s은 이미 존재합니다.".formatted(USERNAME));
        }

        @Test @DisplayName("Email 중복시 EmailAlreadyExistsException 이 발생한다")
        void test03() {
            //given
            given_email_already_exists_exception();

            //expected
            assertThatThrownBy(() -> userService.saveUser(getSaveUserCommand()))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessage("Email=%s은 이미 존재합니다.".formatted(EMAIL));
        }
    }

    @Nested
    @DisplayName("사용자_수정하면_")
    class UpdateUser {

        UpdateUserCommand command = getUpdateUserCommand();
        @Test
        @DisplayName("UpdateUserCommand 객체로 받아 수정한다.")
        void test01() {
            //given
            User user = getUser();
            given_optional_of_nullable_user(user);

            //when
            userService.updateUser(USER_ID, command);

            //then
            assertThat(user.getEmail()).isEqualTo(UPDATE_EMAIL);
            assertThat(user.getName()).isEqualTo(UPDATE_NAME);
            assertThat(user.getRole()).isEqualTo(UPDATE_ROLE);
            assertThat(user.getUserState()).isEqualTo(UPDATE_USER_STATE);

            //verify
            verify(userRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("email 중복되어 UsernameAlreadyExistsException 이 발생한다.")
        void test02() {
            //given
            given(userRepository.existsByEmail(any(String.class))).willReturn(true);

            //expected
            assertThatThrownBy(() -> userService.updateUser(USER_NOT_FOUND_ID, command))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessage(EMAIL_ALREADY_EXISTS_MESSAGE.formatted(command.email()));
        }

        @Test
        @DisplayName("userId = 0L 으로 조회실패하고 UserNotFoundException 이 발생한다.")
        void test03() {
            //given
            given_user_not_found_exception();

            //expected
            assertThatThrownBy(() -> userService.updateUser(USER_NOT_FOUND_ID, command))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage(USER_NOT_FOUND_MESSAGE);
        }
    }
    @Nested
    @DisplayName("사용자_삭제하면_")
    class DeleteUser {

        @Test
        @DisplayName("User Entity 조회하여 UserState 를 DELETE 로 업데이트 한다.")
        void test01() {
            //given
            User user = getUser();
            given_optional_of_nullable_user(user);

            //when
            userService.deleteUser(USER_ID);

            //then
            assertThat(user.getUserState()).isEqualTo(UserState.DELETE);

            //verify
            verify(userRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("userId = 0L 으로 조회실패하고 UserNotFoundException 이 발생한다.")
        void test02() {
            //given
            given_user_not_found_exception();

            //expected
            assertThatThrownBy(() -> userService.deleteUser(USER_NOT_FOUND_ID))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage(USER_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("사용자 회원가입하면_")
    class Join {
        @Test
        @DisplayName("SaveUserCommand 객체를 받아 저장한다.")
        void test01() {
            //given
            User user = getUser();
            given(userRepository.save(any(User.class))).willReturn(user);
            given(userCommandMapper.toUser(any(UserJoinCommand.class))).willReturn(user);
            UserJoinCommand command = getJoinUserCommand();

            //when
            Long joinedUserId = userService.join(command);

            //then

            //verify
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("Username 중복시 UsernameAlreadyExistsException 이 발생한다")
        void test02() {
            //given
            given_username_already_exists_exception();

            //expected
            assertThatThrownBy(() -> userService.join(getJoinUserCommand()))
                    .isInstanceOf(UsernameAlreadyExistsException.class)
                    .hasMessage(USERNAME_ALREADY_EXISTS_MESSAGE.formatted(USERNAME));
        }

        @Test @DisplayName("Email 중복시 EmailAlreadyExistsException 이 발생한다")
        void test03() {
            //given
            given_email_already_exists_exception();

            //expected
            assertThatThrownBy(() -> userService.join(getJoinUserCommand()))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessage(EMAIL_ALREADY_EXISTS_MESSAGE.formatted(EMAIL));
        }
    }

    @Nested
    @DisplayName("사용자_정보조회하면_")
    class FindInfo {
        @Test
        @DisplayName("1건 조회된다.")
        void test01() {
            //given
            User user = getUser();
            given_optional_of_nullable_user(user);

            //when
            User findUser = userService.findInfo(USER_ID);

            //then
            assertThat(findUser.getUsername()).isEqualTo(USERNAME);
            assertThat(findUser.getPassword()).isNotEqualTo(PASSWORD);
            assertThat(findUser.getEmail()).isEqualTo(EMAIL);
            assertThat(findUser.getName()).isEqualTo(NAME);
            assertThat(findUser.getRole()).isEqualTo(ROLE);
            assertThat(findUser.getUserState()).isEqualTo(USER_STATE);

            //verify
            verify(userRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("userId = 0L 으로 조회실패하고 UserNotFoundException 이 발생한다.")
        void test02() {
            //given
            given_user_not_found_exception();

            //expected
            assertThatThrownBy(() -> userService.findInfo(USER_NOT_FOUND_ID))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage(USER_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("사용자_정보수정하면_")
    class UpdateInfo {

        UserUpdateInfoCommand command = getUserUpdateInfoCommand();
        @Test
        @DisplayName("UserUpdateInfoCommand 객체로 받아 수정한다.")
        void test01() {
            //given
            User user = getUser();
            given_optional_of_nullable_user(user);

            //when
            userService.updateInfo(USER_ID, command);

            //then
            assertThat(user.getEmail()).isEqualTo(UPDATE_EMAIL);
            assertThat(user.getName()).isEqualTo(UPDATE_NAME);

            //verify
            verify(userRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("email 중복되어 UsernameAlreadyExistsException 이 발생한다.")
        void test02() {
            //given
            given(userRepository.existsByEmail(any(String.class))).willReturn(true);

            //expected
            assertThatThrownBy(() -> userService.updateInfo(USER_NOT_FOUND_ID, command))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessage(EMAIL_ALREADY_EXISTS_MESSAGE.formatted(command.email()));
        }

        @Test
        @DisplayName("userId = 0L 으로 조회실패하고 UserNotFoundException 이 발생한다.")
        void test03() {
            //given
            given_user_not_found_exception();

            //expected
            assertThatThrownBy(() -> userService.updateInfo(USER_NOT_FOUND_ID, command))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage(USER_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("사용자_비밀번호_확인하면")
    class MatchPassword {
        @Test
        @DisplayName("비밀번호가 일치하여 true를 반환한다.")
        void test01() {
            //given
            User user = getUser();
            given(userRepository.findByUsername(any(String.class))).willReturn(Optional.ofNullable(user));
            given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(Boolean.TRUE);

            String password = PASSWORD;

            //when
            Boolean aBoolean = userService.matchPassword(USERNAME, password);

            //then
            assertThat(aBoolean).isTrue();

            //verify
            verify(userRepository, times(1)).findByUsername(any(String.class));
            verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        }

        @Test
        @DisplayName("비밀번호가 불일치하여 false를 반환한다.")
        void test02() {
            //given
            User user = getUser();
            given(userRepository.findByUsername(any(String.class))).willReturn(Optional.ofNullable(user));
            given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(Boolean.FALSE);

            String notMatchPassword = PASSWORD + "wrong";

            //when
            Boolean aBoolean = userService.matchPassword(USERNAME, notMatchPassword);

            //then
            assertThat(aBoolean).isFalse();

            //verify
            verify(userRepository, times(1)).findByUsername(any(String.class));
            verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        }

        @Test
        @DisplayName("username = taewoong 으로 조회실패하고 UserNotFoundException 이 발생한다.")
        void test03() {
            //given
            given_user_not_found_exception();

            //expected
            assertThatThrownBy(() -> userService.matchPassword(USERNAME_NOT_FOUND, PASSWORD))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage(USERNAME_NOT_FOUND_MESSAGE.formatted(USERNAME_NOT_FOUND));
        }
    }

    private void given_optional_of_nullable_user(User user) {
        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(user));
    }

    private void given_user_not_found_exception() {
        given(userRepository.findById(any(Long.class))).willThrow(new UserNotFoundException(USER_NOT_FOUND_ID));
    }

    private void given_username_already_exists_exception() {
        given(userRepository.existsByUsername(any(String.class))).willThrow(new UsernameAlreadyExistsException(USERNAME));
    }

    private void given_email_already_exists_exception() {
        given(userRepository.existsByEmail(any(String.class))).willThrow(new EmailAlreadyExistsException(EMAIL));
    }
}