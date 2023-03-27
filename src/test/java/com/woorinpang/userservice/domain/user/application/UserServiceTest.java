package com.woorinpang.userservice.domain.user.application;

import com.woorinpang.userservice.domain.user.application.dto.UserCommandMapper;
import com.woorinpang.userservice.domain.user.application.dto.condition.UserSearchCondition;
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

import java.util.Optional;

import static com.woorinpang.userservice.domain.user.UserSetup.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("UserService 단위 테스트")
class UserServiceTest extends UnitTest {

    @InjectMocks protected UserService userService;
    @Mock protected UserQueryRepository userQueryRepository;
    @Mock protected UserRepository userRepository;
    @Mock protected UserCommandMapper userCommandMapper;

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
        @DisplayName("userId = 0L 으로 조회실패하고 UserNotFoundException 이 발생한다.")
        void test02() {
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