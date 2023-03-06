package com.woorinpang.userservice.core.user.application.dto;

import com.woorinpang.userservice.core.user.application.dto.request.SaveUserCommand;
import com.woorinpang.userservice.core.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserCommandMapper {

    UserCommandMapper INSTANCE = Mappers.getMapper(UserCommandMapper.class);

    User toEntity(SaveUserCommand command);
}
