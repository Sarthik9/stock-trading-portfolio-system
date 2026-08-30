package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "SX4CfT9ToB9dESnN5KiqF7oOnMiNnDSoBE7zVnLD01w="
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                3600000L
        );
    }

    @Test
    void shouldGenerateTokenSuccessfully() {

        String token = jwtService.generateToken("Samar");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void generatedTokenShouldContainUsername() {

        String token = jwtService.generateToken("Samar");

        String username = jwtService.validateToken(token);

        assertEquals("Samar", username);
    }

    @Test
    void shouldValidateValidToken() {

        String token = jwtService.generateToken("Samar");

        String username = jwtService.validateToken(token);

        assertEquals("Samar", username);
    }

    @Test
    void shouldRejectInvalidToken() {

        String invalidToken = "invalid.jwt.token";

        String result = jwtService.validateToken(invalidToken);

        assertNull(result);
    }

    @Test
    void shouldRejectTamperedToken() {

        String token = jwtService.generateToken("Samar");

        String tamperedToken = token + "abc";

        String result = jwtService.validateToken(tamperedToken);

        assertNull(result);
    }

    @Test
    void shouldRejectExpiredToken() {

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                -1000L
        );

        String token = jwtService.generateToken("Samar");

        String result = jwtService.validateToken(token);

        assertNull(result);
    }
}
