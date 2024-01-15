package com.manoj.article_hub.user.service;

import com.manoj.article_hub.exception.UserNotFoundException;
import com.manoj.article_hub.exception.WrongCredentialsException;
import com.manoj.article_hub.user.dto.AuthenticationResponse;
import com.manoj.article_hub.user.dto.UserCreationDto;
import com.manoj.article_hub.user.dto.UserDto;
import com.manoj.article_hub.user.dto.UserLoginDto;
import com.manoj.article_hub.user.entity.UserEntity;
import com.manoj.article_hub.user.mapper.UserMapper;
import com.manoj.article_hub.user.repository.UserRepository;
import com.manoj.article_hub.user.utils.UserTestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private static final String FIRST_NAME = "First";
    private static final String LAST_NAME = "Last";
    private static final String EMAIL = "email@email.com";
    private static final String INCORRECT_EMAIL = "fakemail@email.com";
    private static final String PASSWORD = "Password";
    private static final String INCORRECT_PASSWORD = "Incorrect password";
    private UserCreationDto userCreationDto;
    private UserEntity user_saved;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService testee;

    @Before
    public void init() {
        userCreationDto = UserCreationDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        UserEntity user = UserTestUtil.createUserWithoutUserId();
        user_saved = UserTestUtil.createUser();
        UserDto userDto = new UserDto();
        userDto.setFirstName(FIRST_NAME);
        userDto.setLastName(LAST_NAME);
        userDto.setEmail(EMAIL);

        Mockito.when(userMapper.toEntity(Mockito.eq(userCreationDto))).thenReturn(user);
        Mockito.when(userMapper.toDto(Mockito.eq(user_saved))).thenReturn(userDto);
        Mockito.when(userRepository.save(Mockito.eq(user))).thenReturn(user_saved);
        Mockito.when(userRepository.findByUsername(Mockito.eq(EMAIL))).thenReturn(Optional.of(user_saved));
        Mockito.when(userRepository.findById(Mockito.eq(user_saved.getId()))).thenReturn(Optional.of(user_saved));
    }

    @Test
    public void testAddUserIfDataIsEmpty() {
        userCreationDto = null;
        AuthenticationResponse response = testee.addUser(userCreationDto);

        Assert.assertNull(response);
    }

    @Test
    public void testAddUser() {
        AuthenticationResponse result = testee.addUser(userCreationDto);
        Assert.assertNotNull(result);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginUserWhenIncorrectEmail() {
        UserLoginDto userInput = UserLoginDto.builder()
                .email(INCORRECT_EMAIL)
                .password(PASSWORD)
                .build();

        Mockito.when(userRepository.findByUsername(Mockito.eq(userInput.getEmail()))).thenReturn(null);
        testee.loginUser(userInput);
    }
    @Test(expected = WrongCredentialsException.class)
    @Ignore
    public void testLoginUserWhenIncorrectPassword() {
        UserLoginDto userInput = UserLoginDto.builder()
                .email(EMAIL)
                .password(INCORRECT_PASSWORD)
                .build();

        testee.loginUser(userInput);
    }

    @Test
    public void testLoginUser() {
        UserLoginDto userInput = UserLoginDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        AuthenticationResponse result = testee.loginUser(userInput);

//        Assert.assertEquals(FIRST_NAME, result.getFirstName());
//        Assert.assertEquals(LAST_NAME, result.getLastName());
//        Assert.assertEquals(EMAIL, result.getEmail());
    }

    @Test
    public void testCheckUserExist() {
        Optional<UserEntity> result = testee.checkUserExist(1L);

        Assert.assertEquals(Optional.of(user_saved), result);

        result = testee.checkUserExist(2L);

        Assert.assertEquals(Optional.empty(), result);
    }
}
