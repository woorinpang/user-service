package com.woorinpang.userservice.core.user.presentation.dto;

import com.woorinpang.userservice.core.user.application.dto.request.SaveUserCommand;
import com.woorinpang.userservice.core.user.presentation.dto.request.SaveUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    @Mappings({
            @Mapping(source = "request.roleCode", target = "role"),
            @Mapping(source = "request.userStateCode", target = "userState")
    })
    SaveUserCommand toCommand(SaveUserRequest request);

}
