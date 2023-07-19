package com.manoj.article_hub.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return getPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String encryptPassword(String password) {
        return getPasswordEncoder().encode(password);
    }
}
