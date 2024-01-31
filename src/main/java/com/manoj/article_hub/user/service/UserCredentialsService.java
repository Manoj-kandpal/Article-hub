package com.manoj.article_hub.user.service;

import com.manoj.article_hub.exception.WrongCredentialsException;
import com.manoj.article_hub.user.entity.UserCredentialsEntity;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.repository.UserCredentialsRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                .password(encryptionService.encryptPassword(password))
                .build();

        return userCredentialsRepository.save(userCredentialsEntity);
    }

    public void verifyUser(UserEntity userEntity, String enteredPassword) {
        Validate.notNull(userEntity, "User can't be null");
        Validate.notNull(enteredPassword, "Password must be provided.");

        Optional<UserCredentialsEntity> userCredentialsEntity = userCredentialsRepository.findById(userEntity.getId());

        userCredentialsEntity.ifPresent(credentialsEntity -> encryptionService.verifyPassword(enteredPassword, credentialsEntity.getPassword()));
    }


}
