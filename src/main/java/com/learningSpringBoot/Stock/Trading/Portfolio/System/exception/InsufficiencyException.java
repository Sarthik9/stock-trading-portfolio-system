package com.learningSpringBoot.Stock.Trading.Portfolio.System.exception;

public class InsufficiencyException extends RuntimeException{

    public InsufficiencyException(String message){
        super(message);
    }

}
