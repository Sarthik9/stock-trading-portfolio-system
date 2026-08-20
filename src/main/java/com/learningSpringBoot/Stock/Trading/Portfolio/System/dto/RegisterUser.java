package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.Role;
import jakarta.validation.constraints.NotNull;

public class RegisterUser {

    @NotNull(message = "username is required")
    private String username;

    @NotNull(message = "email is required")
    private String email;

    @NotNull(message = "password is required")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
