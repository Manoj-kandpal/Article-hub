package com.manoj.article_hub.user.service;

import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manoj.article_hub.user.entity.UserCredentialsEntity;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.repository.UserCredentialsRepository;

@Service
public class UserCredentialsService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private EncryptionService encryptionService;

    public UserCredentialsEntity create(UserEntity userEntity, String password) {
        Validate.notNull(userEntity, "User can't be null");

        UserCredentialsEntity userCredentialsEntity = UserCredentialsEntity.builder()
                .user(userEntity)
                .hashedPassword(encryptionService.encryptPassword(password))
                .build();

        return userCredentialsRepository.save(userCredentialsEntity);
    }

    public Boolean verifyUser(UserEntity userEntity, String enteredPassword) {
        Validate.notNull(userEntity, "User can't be null");
        Validate.notNull(enteredPassword, "Password must be provided.");

        Optional<UserCredentialsEntity> userCredentialsEntity = userCredentialsRepository.findById(userEntity.getId());

        return userCredentialsEntity.map(entity -> encryptionService.verifyPassword(enteredPassword, entity.getHashedPassword())).orElse(false);
    }

}
