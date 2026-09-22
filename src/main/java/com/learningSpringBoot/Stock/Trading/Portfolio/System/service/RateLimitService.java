package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private static final Duration WINDOW = Duration.ofMinutes(1);
    private static final int MAX_REQUESTS = 5;

    private static final Logger log = LoggerFactory.getLogger(RateLimitService.class);

    private final StringRedisTemplate redisTemplate;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId) {

        String key = "rate_limit:" + userId;

        try {
            Long count = redisTemplate.opsForValue().increment(key);

            if (count != null && count == 1) {
                redisTemplate.expire(key, WINDOW);
            }
            return count != null && count <= MAX_REQUESTS;
        } catch (RedisConnectionFailureException e) {
            // Handle Redis connection failure gracefully
            // Fail-open: allow request if Redis is unavailable
            log.error("Redis connection failure while checking rate limit for user {}: {}", userId, e.getMessage());
            return true; // Allow the request if Redis is down
        }
    }
}
