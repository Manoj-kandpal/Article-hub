package com.manoj.article_hub.user.controller;

import com.manoj.article_hub.exception.UserNotFoundException;
import com.manoj.article_hub.user.dto.AuthenticationResponse;
import com.manoj.article_hub.user.dto.UserCreationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manoj.article_hub.common.HasLogger;
import com.manoj.article_hub.user.dto.UserLoginDto;
import com.manoj.article_hub.user.service.UserService;

@RestController
@RequestMapping(UserController.ENDPOINT_PATH_ROOT)
public class UserController implements HasLogger {
    public static final String ENDPOINT_PATH_ROOT = "/api/user";
    public static final String ENDPOINT_PATH_LOGIN = "/login";
    public static final String ENDPOINT_PATH_REGISTER = "/register";
    public static final String ENDPOINT_PATH_UPDATE = "/update";

    @Autowired
    private UserService userService;

    @PostMapping(ENDPOINT_PATH_REGISTER)
    public ResponseEntity<AuthenticationResponse> addUser(@RequestBody UserCreationDto userInput) {
        try {
            AuthenticationResponse response = userService.addUser(userInput);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            getLogger().error("User creation failed, reason: {}. {}", e.getMessage(), e.getCause().getCause().getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ENDPOINT_PATH_LOGIN)
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto) {
        try {
            AuthenticationResponse response = userService.loginUser(userLoginDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            getLogger().error("User Login failed, reason: {}.", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            getLogger().error("User Login failed, reason: {}.", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // todo:
//    updating user should be logged in first
//    what attributes we want to update( keeping in mind that email or something should not be allowed to change)

//    @PutMapping(ENDPOINT_PATH_UPDATE)
//    public ResponseEntity<?> updateUser(@RequestBody )
}
