package com.manoj.article_hub.exception;

public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException() {
        super("E-mail or Password incorrect. Please login again or try resetting your password.");
    }
}
