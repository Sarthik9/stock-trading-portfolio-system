package com.learningSpringBoot.Stock.Trading.Portfolio.System.security;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // Apply rate limiting to /orders/createOrder
        if ("/orders/createOrder".equals(uri) && "POST".equals(method)) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null &&
                    authentication.isAuthenticated() &&
                    !("anonymousUser").equals(authentication.getPrincipal())) {
                String userId = authentication.getName();

                if (!rateLimitService.isAllowed(userId)) {
                    response.setStatus(429);
                    response.getWriter().write("Rate limit exceeded");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
