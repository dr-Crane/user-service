package com.example.user.service.mapper;

import com.example.user.service.dal.entity.UserEntity;
import com.example.user.service.dto.CreateUserDto;
import com.example.user.service.dto.UpdateUserDto;
import com.example.user.service.dto.UserShortInfoDto;
import io.micrometer.common.util.StringUtils;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "password", expression = "java(encoder.encode(dto.getPassword()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity map(CreateUserDto dto, @Context PasswordEncoder encoder);

    UserShortInfoDto map(UserEntity entity);

    void update(
            @MappingTarget UserEntity entity,
            UpdateUserDto dto,
            @Context PasswordEncoder encoder
    );

    @AfterMapping
    default void mapPassword(
            @MappingTarget UserEntity ignoredEntity,
            UpdateUserDto dto,
            @Context PasswordEncoder encoder
    ) {
        if (StringUtils.isNotEmpty(dto.getPassword())) {
            encoder.encode(dto.getPassword());
        }
    }

}
