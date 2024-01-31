package com.manoj.article_hub.user.service;

import java.util.Optional;

import com.manoj.article_hub.exception.UserNotFoundException;
import com.manoj.article_hub.security.JwtService;
import com.manoj.article_hub.user.dto.AuthenticationResponse;
import com.manoj.article_hub.user.dto.UserCreationDto;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manoj.article_hub.common.HasLogger;
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
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserCredentialsService userCredentialsService;

    @Transactional
    public AuthenticationResponse addUser(UserCreationDto data) {
        if (data == null) {
            getLogger().error("User can't be created if data is null");
            return null;
        }
        UserEntity entity = userMapper.toEntity(data);
        UserEntity savedEntity = userRepository.save(entity);
        userCredentialsService.create(savedEntity, data.getPassword());
        getLogger().info("User Created: {}", savedEntity.getFirstName());

        String jwtToken = jwtService.generateTokenWithNameAndEmail(savedEntity);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional(readOnly = true)
    public AuthenticationResponse loginUser(UserLoginDto data) {
        Validate.notNull(data, "Login details can't be null");

        UserEntity userEntity = userRepository.findByUsername(data.getEmail()).orElseThrow(() -> {
            getLogger().info(USER_NOT_FOUND, data.getEmail());
            return new UserNotFoundException(data.getEmail());
        });

//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));

        userCredentialsService.verifyUser(userEntity, data.getPassword());

        String jwt = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> checkUserExist(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> checkUserExist(String username) {
        return userRepository.findByUsername(username);
    }

    // todo: handle ExpiredJwtException somewhere, while fetching anything..
	// todo: setCurrentUserDetailsAndTokenExpiry has to be updated for the localStorage.setItem()
}
