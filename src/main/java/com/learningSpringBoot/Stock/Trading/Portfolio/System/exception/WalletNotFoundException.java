package com.learningSpringBoot.Stock.Trading.Portfolio.System.exception;

public class WalletNotFoundException extends RuntimeException{

    public WalletNotFoundException(String message){
        super(message);
    }

}
