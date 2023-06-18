package com.manoj.article_hub.user.service;

import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manoj.article_hub.common.HasLogger;
import com.manoj.article_hub.exception.WrongCredentialsException;
import com.manoj.article_hub.user.dto.UserCreationDto;
import com.manoj.article_hub.user.dto.UserDto;
import com.manoj.article_hub.user.dto.UserLoginDto;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.mapper.UserMapper;
import com.manoj.article_hub.user.repository.UserRepository;

@Service
public class UserService implements HasLogger {

    private static final String USER_NOT_FOUND = "User with email '{}' doesn't exist. Please create an account first.";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCredentialsService userCredentialsService;

    @Transactional
    public UserDto addUser(UserCreationDto data) {
        if (data == null) {
            getLogger().error("User can't be created if data is null");
            return null;
        }
        UserEntity entity = userMapper.toEntity(data);
        UserEntity savedEntity = userRepository.save(entity);
        userCredentialsService.create(savedEntity, data.getPassword());
        getLogger().info("User Created: {}", savedEntity.getFirstName());

        return userMapper.toDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public UserDto loginUser(UserLoginDto data) {
        Validate.notNull(data, "Login details can't be null");

        UserEntity userEntity = userRepository.getUserByUsername(data.getEmail());
        if (userEntity == null) {
            getLogger().info(USER_NOT_FOUND, data.getEmail());
            return null;
        }
        if (userCredentialsService.verifyUser(userEntity, data.getPassword())) {
            return userMapper.toDto(userEntity);
        } else {
            throw new WrongCredentialsException();
        }
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> checkUserExist(Long id) {
        return userRepository.findById(id);
    }
}
