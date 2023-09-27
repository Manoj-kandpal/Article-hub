package com.manoj.article_hub.user.service;

import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.utils.UserTestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserCredentialsServiceTest {

    private static final String CORRECT_PASSWORD = "Sample Password";
    private static final String INCORRECT_PASSWORD = "Wrong Password";
    private static final String ENCRYPTED_PASSWORD = "Encrypted Password";
    private UserEntity user;
    private UserCredentialsEntity userCredentials;
    private UserCredentialsEntity userCredentials_saved;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @InjectMocks
    private UserCredentialsService testee;

    @Before
    public void init() {
        user = UserTestUtil.createUser();
        userCredentials = UserCredentialsEntity.builder()
                .user(user)
                .hashedPassword(ENCRYPTED_PASSWORD)
                .build();
        userCredentials_saved = UserCredentialsEntity.builder()
                .user(user)
                .id(1L)
                .hashedPassword(ENCRYPTED_PASSWORD)
                .build();

        Mockito.when(userCredentialsRepository.save(Mockito.eq(userCredentials))).thenReturn(userCredentials_saved);
        Mockito.when(encryptionService.encryptPassword(CORRECT_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
        Mockito.when(encryptionService.verifyPassword(CORRECT_PASSWORD, ENCRYPTED_PASSWORD)).thenReturn(true);
        Mockito.when(encryptionService.verifyPassword(INCORRECT_PASSWORD, ENCRYPTED_PASSWORD)).thenReturn(false);
    }

    @Test
    public void testCreate() {
        UserCredentialsEntity result = testee.create(user, CORRECT_PASSWORD);

        Assert.assertNotNull(result);
        Assert.assertEquals(ENCRYPTED_PASSWORD, result.getHashedPassword());
        Assert.assertEquals(user, result.getUser());
        Mockito.verify(userCredentialsRepository, Mockito.times(1)).save(userCredentials);
    }

    @Test
    public void testVerifyUserForCorrectPassword() {
        Mockito.when(userCredentialsRepository.findById(Mockito.eq(user.getId()))).thenReturn(Optional.of(userCredentials_saved));

        Boolean result = testee.verifyUser(user, CORRECT_PASSWORD);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testVerifyUserForIncorrectPassword() {
        Mockito.when(userCredentialsRepository.findById(Mockito.eq(user.getId()))).thenReturn(Optional.of(userCredentials_saved));

        Boolean result = testee.verifyUser(user, INCORRECT_PASSWORD);

        Assert.assertEquals(false, result);
    }
}
