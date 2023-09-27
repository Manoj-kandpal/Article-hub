package com.manoj.article_hub.user.mapper;

import com.manoj.article_hub.user.dto.UserCreationDto;
import com.manoj.article_hub.user.entity.Role;
import com.manoj.article_hub.user.service.EncryptionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.manoj.article_hub.common.HasLogger;
import com.manoj.article_hub.config.MapperConfig;
import com.manoj.article_hub.user.dto.UserDto;
import com.manoj.article_hub.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapperConfig.class)
public abstract class UserMapper implements HasLogger {

    @Autowired
    private EncryptionService encryptionService;

    @Mapping(target = "email", source = "username")
    public abstract UserDto toDto(UserEntity userEntity);

    public UserEntity toEntity(UserCreationDto data) {
        return UserEntity.builder()
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .username(data.getEmail())
                .password(encryptionService.encryptPassword(data.getPassword()))
                .role(Role.USER)
                .build();
    }
}
