package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private static final Duration WINDOW = Duration.ofMinutes(1);
    private static final int MAX_REQUESTS = 5;

    private final StringRedisTemplate redisTemplate;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId) {

        String key = "rate_limit:" + userId;

        Long count= redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1){
            redisTemplate.expire(key, WINDOW);
        }
        return count!=null && count <= MAX_REQUESTS;
    }
}
