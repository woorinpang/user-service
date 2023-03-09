package com.woorinpang.userservice.core.user.infrastructure;

import com.woorinpang.userservice.core.user.UserSetup;
import com.woorinpang.userservice.core.user.UserTestConfig;
import com.woorinpang.userservice.core.user.domain.Role;
import com.woorinpang.userservice.core.user.domain.User;
import com.woorinpang.userservice.core.user.domain.UserRepository;
import com.woorinpang.userservice.core.user.domain.UserState;
import com.woorinpang.userservice.core.user.dto.UserListDto;
import com.woorinpang.userservice.core.user.dto.UserSearchCondition;
import com.woorinpang.userservice.global.dto.CommonSearchCondition;
import com.woorinpang.userservice.test.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(UserTestConfig.class)
class UserQueryRepositoryTest extends RepositoryTest {

    @Autowired protected UserQueryRepository userQueryRepository;
    @Autowired protected UserRepository userRepository;

    @Nested
    class 회원_목록_ {

        UserSearchCondition condition = new UserSearchCondition();
        PageRequest pageRequest = PageRequest.of(0, 10);

        @BeforeEach
        void beforeEach() {
            List<User> users = UserSetup.setUsers();
            userRepository.saveAll(users);
        }

        @Test
        void 열개_조회한다() {
            //given

            //when
            Page<UserListDto> content = userQueryRepository.findUsers(condition, pageRequest);

            //then
            assertThat(content.getContent().size()).isEqualTo(10);
        }

        @Test
        void 이름을_조회한다() {
            //given
            condition.setSearchKeywordType(CommonSearchCondition.KeywordType.NAME);
            condition.setSearchKeyword("A");

            //when
            Page<UserListDto> content = userQueryRepository.findUsers(condition, pageRequest);

            //then
            assertThat(content.getContent().size()).isEqualTo(5);
        }

        @Test
        void 권한코드을_조회한다() {
            //given
            condition.setSearchRole(Role.USER);

            //when
            Page<UserListDto> content = userQueryRepository.findUsers(condition, pageRequest);

            //then
            assertThat(content.getContent().size()).isEqualTo(10);
        }

        @Test
        void 사용자_상태코드를_조회한다() {
            //given
            condition.setSearchUserState(UserState.NORMAL);

            //when
            Page<UserListDto> content = userQueryRepository.findUsers(condition, pageRequest);

            //then
            assertThat(content.getContent().size()).isEqualTo(10);
        }

        @Test
        void 조회된_목록이_없다() {
            //given
            condition.setSearchKeywordType(CommonSearchCondition.KeywordType.NAME);
            condition.setSearchKeyword("조회되지 못하게 검색 키워드를 넣는다");

            //when
            Page<UserListDto> content = userQueryRepository.findUsers(condition, pageRequest);

            //then
            assertThat(content.getContent().size()).isZero();
        }
    }
}