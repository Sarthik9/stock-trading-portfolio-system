package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.LoginRequest;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.RegisterUser;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.UserEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.UserAlreadyExistsException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.UserNotFoundException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.Role;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.UserRepository;
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

    @Autowired
    private JwtService jwtService;

    public void register(RegisterUser request) {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserAlreadyExistsException("Username already exists");
        }
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now().toString());
        userEntity.setRole(Role.USER);

        userRepository.save(userEntity);
    }

    public String login(LoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        boolean exists = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (exists){
            String token = jwtService.generateToken(request.getUsername());
            return "token : " + token;
        }
        else throw new UserNotFoundException("Invalid password");
    }
}
