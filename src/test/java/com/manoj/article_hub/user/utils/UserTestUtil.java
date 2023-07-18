package com.manoj.article_hub.user.utils;

import com.manoj.article_hub.user.entity.UserEntity;

/**
 * Utility class that provides User related helper methods.
 */
public class UserTestUtil {
    public static final String FIRST_NAME = "User 1";
    public static final String EMAIL = "username.com";
    public static final Long USER_ID = 1L;
    public static UserEntity createUser () {
        return UserEntity.builder()
                .id(USER_ID)
                .firstName(FIRST_NAME)
                .username(EMAIL)
                .build();
    }
}
