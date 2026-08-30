package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.LoginRequest;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.RegisterUser;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.UserEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.UserAlreadyExistsException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.UserNotFoundException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.Role;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAuthServiceTest {

    @InjectMocks
    private UserAuthService userAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    // Registration Tests
    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterUser request = new RegisterUser();
        request.setUsername("testuser");
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        userAuthService.register(request);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        UserEntity savedUser = captor.getValue();
        // Add assertions to verify the properties of the savedUser if needed
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@gmail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void shouldEncodePassword() {
        RegisterUser request = new RegisterUser();
        request.setUsername("testuser");
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        userAuthService.register(request);

        verify(passwordEncoder).encode("password");

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        UserEntity savedUser = captor.getValue();
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void shouldAssignUSERRole() {

        // Arrange
        RegisterUser request = new RegisterUser();
        request.setUsername("Samar");
        request.setEmail("samar@gmail.com");
        request.setPassword("qwerty");

        when(userRepository.existsByUsername("Samar"))
                .thenReturn(false);

        when(userRepository.existsByEmail("samar@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("qwerty"))
                .thenReturn("encodedPassword");

        // Act
        userAuthService.register(request);

        // Assert
        ArgumentCaptor<UserEntity> captor =
                ArgumentCaptor.forClass(UserEntity.class);

        verify(userRepository).save(captor.capture());

        assertEquals(Role.USER, captor.getValue().getRole());
    }

    @Test
    void shouldRejectDuplicateUsername() {

        // Arrange
        RegisterUser request = new RegisterUser();
        request.setUsername("Samar");
        request.setEmail("samar@gmail.com");
        request.setPassword("qwerty");

        when(userRepository.existsByUsername("Samar"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                UserAlreadyExistsException.class,
                () -> userAuthService.register(request)
        );
    }

    @Test
    void shouldRejectDuplicateEmail() {

        // Arrange
        RegisterUser request = new RegisterUser();
        request.setUsername("Samar");
        request.setEmail("samar@gmail.com");
        request.setPassword("qwerty");

        when(userRepository.existsByUsername("Samar"))
                .thenReturn(false);

        when(userRepository.existsByEmail("samar@gmail.com"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                UserAlreadyExistsException.class,
                () -> userAuthService.register(request)
        );

        verify(userRepository, never())
                .save(any(UserEntity.class));
    }

    // Login Tests
    @Test
    void shouldLoginSuccessfully() {

        // Arrange
        UUID userId = UUID.randomUUID();

        LoginRequest request = new LoginRequest();
        request.setUsername("Samar");
        request.setPassword("qwerty");

        UserEntity user = new UserEntity();
        user.setUserId(userId);
        user.setUsername("Samar");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("Samar"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "qwerty",
                "encodedPassword"
        )).thenReturn(true);

        when(jwtService.generateToken("Samar"))
                .thenReturn("jwt-token");

        // Act
        String result = userAuthService.login(request);

        // Assert
        assertTrue(result.contains(userId.toString()));
        assertTrue(result.contains("jwt-token"));
    }

    @Test
    void shouldGenerateJwtAfterSuccessfulLogin() {

        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("Samar");
        request.setPassword("qwerty");

        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID());
        user.setUsername("Samar");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("Samar"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "qwerty",
                "encodedPassword"
        )).thenReturn(true);

        when(jwtService.generateToken("Samar"))
                .thenReturn("jwt-token");

        // Act
        userAuthService.login(request);

        // Assert
        verify(jwtService).generateToken("Samar");
    }

    @Test
    void shouldRejectInvalidPassword() {

        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("Samar");
        request.setPassword("wrong");

        UserEntity user = new UserEntity();
        user.setUsername("Samar");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("Samar"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrong",
                "encodedPassword"
        )).thenReturn(false);

        // Act + Assert
        assertThrows(
                UserNotFoundException.class,
                () -> userAuthService.login(request)
        );

        verify(jwtService, never())
                .generateToken(anyString());
    }

    @Test
    void shouldRejectUnknownUser() {

        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("Unknown");
        request.setPassword("qwerty");

        when(userRepository.findByUsername("Unknown"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                UserNotFoundException.class,
                () -> userAuthService.login(request)
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(anyString());
    }
}
