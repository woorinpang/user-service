package com.woorinpang.userservice.domain.user.infrastructure;

import com.woorinpang.userservice.domain.user.UserSetup;
import com.woorinpang.userservice.domain.user.UserTestConfig;
import com.woorinpang.userservice.domain.user.application.dto.condition.UserSearchCondition;
import com.woorinpang.userservice.domain.user.domain.Role;
import com.woorinpang.userservice.domain.user.domain.User;
import com.woorinpang.userservice.domain.user.domain.UserState;
import com.woorinpang.userservice.domain.user.infrastructure.dto.FindPageUserDto;
import com.woorinpang.userservice.test.RepositoryTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(UserTestConfig.class)
@DisplayName("user 레포지토리 테스트")
class UserQueryRepositoryTest extends RepositoryTest {

    @Autowired protected UserQueryRepository userQueryRepository;
    @Autowired protected UserRepository userRepository;

    @BeforeAll
    void init() {
        List<User> users = UserSetup.getUsers();
        userRepository.saveAll(users);
    }

    @Nested
    @DisplayName("사용자_목록_조회하면_반환값은_")
    class FindPageUsersWithValue {
        PageRequest pageRequest = PageRequest.of(0, 10);

        @Test
        @DisplayName("10개 컬럼값이 반환된다.")
        void test01() {
            //given
            UserSearchCondition condition = UserSearchCondition.builder()
                    .build();

            //when
            Page<FindPageUserDto> content = userQueryRepository.findPageUsers(condition, pageRequest);

            //then
            assertThat(content).extracting("userId").first().isInstanceOf(Long.class);
            assertThat(content).extracting("username").first().isInstanceOf(String.class);
            assertThat(content).extracting("email").first().isInstanceOf(String.class);
            assertThat(content).extracting("name").first().isInstanceOf(String.class);
            assertThat(content).extracting("roleCode").first().isInstanceOf(String.class);
            assertThat(content).extracting("roleDescription").first().isInstanceOf(String.class);
            assertThat(content).extracting("userStateCode").first().isInstanceOf(String.class);
            assertThat(content).extracting("userStateDescription").first().isInstanceOf(String.class);
            assertThat(content).extracting("lastLoginDate").first().isNull();
            assertThat(content).extracting("loginFailCount").first().isInstanceOf(Integer.class);
        }
    }


    @Nested
    @DisplayName("사용자_목록_조회하면_")
    class FindPageUsers {

        PageRequest pageRequest = PageRequest.of(0, 10);

        @Test
        @DisplayName("조건없이 10건 조회한다.")
        void test01() {
            //given
            UserSearchCondition condition = UserSearchCondition.builder()
                    .build();

            //when
            Page<FindPageUserDto> content = userQueryRepository.findPageUsers(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(10);
            assertThat(content.getTotalPages()).isEqualTo(1);
            assertThat(content.getContent().size()).isEqualTo(10);
        }

        @Test
        @DisplayName("이름 'A'를 조회하여 5건 조회한다.")
        void test02() {
            //given
            UserSearchCondition condition = UserSearchCondition.builder()
                    .searchKeywordType(UserSearchCondition.KeywordType.NAME)
                    .searchKeyword("A")
                    .build();

            //when
            Page<FindPageUserDto> content = userQueryRepository.findPageUsers(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(5);
            assertThat(content.getTotalPages()).isEqualTo(1);
            assertThat(content.getContent().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("권한 'USER'를 조회하여 5건 조회한다.")
        void test03() {
            //given
            UserSearchCondition condition = UserSearchCondition.builder()
                    .searchRole(Role.USER)
                    .build();
            
            //when
            Page<FindPageUserDto> content = userQueryRepository.findPageUsers(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(5);
            assertThat(content.getTotalPages()).isEqualTo(1);
            assertThat(content.getContent().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("유저상태 'NORMAL'를 조회하여 5건 조회한다.")
        void test04() {
            //given
            UserSearchCondition condition = UserSearchCondition.builder()
                    .searchUserState(UserState.NORMAL)
                    .build();

            //when
            Page<FindPageUserDto> content = userQueryRepository.findPageUsers(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(5);
            assertThat(content.getTotalPages()).isEqualTo(1);
            assertThat(content.getContent().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("맞는 조건이 없어 0건 조회한다.")
        void 조회된_목록이_없다() {
            //given
            UserSearchCondition condition = UserSearchCondition.builder()
                    .searchKeywordType(UserSearchCondition.KeywordType.NAME)
                    .searchKeyword("김수한무거북이와두루미")
                    .searchRole(Role.USER)
                    .searchUserState(UserState.NORMAL)
                    .build();

            //when
            Page<FindPageUserDto> content = userQueryRepository.findPageUsers(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isZero();
            assertThat(content.getTotalPages()).isZero();
            assertThat(content.getContent().size()).isZero();
        }
    }
}