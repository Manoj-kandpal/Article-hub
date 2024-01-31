package com.manoj.article_hub.user.service;

import com.manoj.article_hub.exception.WrongCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    public void verifyPassword(String rawPassword, String encodedPassword) {
        boolean correctPassword = getPasswordEncoder().matches(rawPassword, encodedPassword);
        if(!correctPassword){
            throw new WrongCredentialsException();
        }
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String encryptPassword(String password) {
        return getPasswordEncoder().encode(password);
    }
}
