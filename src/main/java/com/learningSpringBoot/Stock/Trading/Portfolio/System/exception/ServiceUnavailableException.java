package com.learningSpringBoot.Stock.Trading.Portfolio.System.exception;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String message) { super(message); }
}
