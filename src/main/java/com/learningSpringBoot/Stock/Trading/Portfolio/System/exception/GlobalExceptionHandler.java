package com.learningSpringBoot.Stock.Trading.Portfolio.System.exception;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficiencyException.class)
    public ResponseEntity<ErrorResponse> handleInsufficiencyException(InsufficiencyException ex){
        return ResponseEntity.status(400)
                .body( new ErrorResponse( ex.getMessage(), 400) );
    }

    @ExceptionHandler(PortfolioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePortfolioException(PortfolioNotFoundException ex){
        return ResponseEntity.status(404)
                .body( new ErrorResponse(ex.getMessage(), 404) );
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWalletException(WalletNotFoundException ex){
        return ResponseEntity.status(404)
                .body( new ErrorResponse(ex.getMessage(), 404) );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentException(MethodArgumentNotValidException ex){
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(400)
                .body( new ErrorResponse(msg, 404));
    }

}
