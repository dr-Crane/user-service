package com.example.user.service.mapper;

import com.example.user.service.dal.entity.UserEntity;
import com.example.user.service.dto.CreateUserDto;
import com.example.user.service.dto.UserShortInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity map(CreateUserDto dto);

    UserShortInfoDto map(UserEntity entity);

}
