package com.learningSpringBoot.Stock.Trading.Portfolio.System.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
