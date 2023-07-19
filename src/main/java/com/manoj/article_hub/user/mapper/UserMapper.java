package com.manoj.article_hub.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.manoj.article_hub.common.HasLogger;
import com.manoj.article_hub.common.MapperConfig;
import com.manoj.article_hub.user.dto.UserCreationDto;
import com.manoj.article_hub.user.dto.UserDto;
import com.manoj.article_hub.user.entity.UserEntity;

@Mapper(config = MapperConfig.class)
public abstract class UserMapper implements HasLogger {

    @Mapping(target = "email", source = "username")
    public abstract UserDto toDto(UserEntity userEntity);

    public UserEntity toEntity(UserCreationDto data) {
        return UserEntity.builder()
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .username(data.getEmail())
                .build();
    }
}
