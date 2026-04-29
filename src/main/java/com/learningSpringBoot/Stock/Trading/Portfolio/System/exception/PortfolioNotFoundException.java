package com.learningSpringBoot.Stock.Trading.Portfolio.System.exception;

public class PortfolioNotFoundException extends RuntimeException{

    public PortfolioNotFoundException(String message){
        super(message);
    }

}
