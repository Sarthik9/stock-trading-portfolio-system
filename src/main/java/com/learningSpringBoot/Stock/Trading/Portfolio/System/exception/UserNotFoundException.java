package com.learningSpringBoot.Stock.Trading.Portfolio.System.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
