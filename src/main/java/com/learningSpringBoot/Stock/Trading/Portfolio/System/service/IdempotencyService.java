package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.OrderResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class IdempotencyService {

    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public IdempotencyService(StringRedisTemplate stringRedisTemplate,
                              ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public OrderResponse getExistingResponse(UUID userId, String idempotencyKey) {

        String key = buildKey(userId, idempotencyKey);

        String cachedResponse = stringRedisTemplate.opsForValue().get(key);
        if (cachedResponse == null) {
            return null;
        }

        try{
            return objectMapper.readValue(
                    cachedResponse,
                    OrderResponse.class);
        } catch(JsonProcessingException e){
            throw new RuntimeException("Failed to deserialize idempotency response", e);
        }
    }

    public void saveResponse(UUID userId, String idempotencyKey, OrderResponse response) {
        String key = buildKey(userId, idempotencyKey);

        try {
            String json = objectMapper.writeValueAsString(response);
            stringRedisTemplate.opsForValue().set(key, json, TTL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize idempotency response", e);
        }
    }

    private String buildKey(UUID userId, String idempotencyKey) {
        return "idempotency:" +  userId.toString() + ":" + idempotencyKey;
    }
}
