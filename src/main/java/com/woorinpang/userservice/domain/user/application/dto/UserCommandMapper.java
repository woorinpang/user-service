package com.woorinpang.userservice.domain.user.application.dto;

import com.woorinpang.userservice.domain.user.application.dto.request.SaveUserCommand;
import com.woorinpang.userservice.domain.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserCommandMapper {

    UserCommandMapper INSTANCE = Mappers.getMapper(UserCommandMapper.class);

    User toUser(SaveUserCommand command);
}
