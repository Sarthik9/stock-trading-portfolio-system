package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.RegisterUser;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.UserEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.Role;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(@Valid RegisterUser request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now().toString());
        userEntity.setRole(Role.ADMIN);

        userRepository.save(userEntity);
    }
}
