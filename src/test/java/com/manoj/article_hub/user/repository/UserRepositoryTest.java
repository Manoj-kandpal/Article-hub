package com.manoj.article_hub.user.repository;

import com.manoj.article_hub.IntegrationTest;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.utils.UserTestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetUserByUsername() {
        userRepository.deleteById(UserTestUtil.USER_ID);
        userRepository.saveAndFlush(UserTestUtil.createUser());

        UserEntity result = userRepository.getUserByUsername(UserTestUtil.EMAIL);

//        Assert.assertEquals(UserTestUtil.USER_ID, result.getId());
        Assert.assertEquals(UserTestUtil.FIRST_NAME, result.getFirstName());
        Assert.assertNull(result.getLastName());

    }
}
