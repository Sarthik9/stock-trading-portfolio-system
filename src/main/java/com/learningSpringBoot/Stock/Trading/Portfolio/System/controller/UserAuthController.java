package com.learningSpringBoot.Stock.Trading.Portfolio.System.controller;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.RegisterUser;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/registerUser")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterUser request) {
        userAuthService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }
}
