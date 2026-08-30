package com.learningSpringBoot.Stock.Trading.Portfolio.System.security;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.CustomUserDetailsService;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(
                jwtService,
                userDetailsService
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderIsMissing()
            throws ServletException, IOException {

        // Arrange
        when(request.getHeader("Authorization"))
                .thenReturn(null);

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);

        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);

        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );
    }

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderIsNotBearer()
            throws ServletException, IOException {

        // Arrange
        when(request.getHeader("Authorization"))
                .thenReturn("Basic abc123");

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);

        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);

        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );
    }

    @Test
    void shouldAuthenticateUserWhenJwtIsValid()
            throws ServletException, IOException {

        // Arrange
        String token = "valid-jwt";
        String username = "Samar";

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(username)
                        .password("encodedPassword")
                        .roles("USER")
                        .build();

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.validateToken(token))
                .thenReturn(username);

        when(userDetailsService.loadUserByUsername(username))
                .thenReturn(userDetails);

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        var authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
        assertEquals(username, authentication.getName());
        assertTrue(
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                                authority ->
                                        authority.getAuthority()
                                                .equals("ROLE_USER")
                        )
        );

        verify(jwtService).validateToken(token);
        verify(userDetailsService)
                .loadUserByUsername(username);

        verify(filterChain)
                .doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateUserWhenJwtIsInvalid()
            throws ServletException, IOException {

        // Arrange
        String token = "invalid-jwt";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtService.validateToken(token))
                .thenReturn(null);

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        verify(jwtService).validateToken(token);

        verifyNoInteractions(userDetailsService);

        verify(filterChain)
                .doFilter(request, response);
    }

    @Test
    void shouldNotOverwriteExistingAuthentication()
            throws ServletException, IOException {

        // Arrange
        var existingAuthentication =
                new UsernamePasswordAuthenticationToken(
                        "ExistingUser",
                        null,
                        java.util.List.of(
                                new SimpleGrantedAuthority("ROLE_USER")
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(existingAuthentication);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer valid-jwt");

        when(jwtService.validateToken("valid-jwt"))
                .thenReturn("Samar");

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        assertSame(
                existingAuthentication,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        verify(jwtService).validateToken("valid-jwt");

        verifyNoInteractions(userDetailsService);

        verify(filterChain)
                .doFilter(request, response);
    }
}
