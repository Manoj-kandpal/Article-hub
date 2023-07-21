package com.manoj.article_hub.user.service;

import com.manoj.article_hub.user.repository.UserCredentialsRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserCredentialsServiceTest {

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @InjectMocks
    private UserCredentialsService testee;

    @BeforeEach
    public void init() {
    }

    @Test
    public void test() {

    }
}
