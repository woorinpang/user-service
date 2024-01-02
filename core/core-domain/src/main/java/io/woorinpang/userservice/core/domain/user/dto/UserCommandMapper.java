package io.woorinpang.userservice.core.domain.user.dto;

import com.woorinpang.userservice.domain.user.domain.UserTemp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserCommandMapper {

    UserCommandMapper INSTANCE = Mappers.getMapper(UserCommandMapper.class);

    UserTemp toUser(SaveUserCommand command);

    UserTemp toUser(UserJoinCommand command);
}
