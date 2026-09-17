package com.learningSpringBoot.Stock.Trading.Portfolio.System.exception;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

public class CacheConfigHandler implements CacheErrorHandler {

            @Override
            public void handleCacheGetError(RuntimeException exception,
                                            Cache cache,
                                            Object key) {
                System.err.println("Cache get error: " + "Key : " + key + " Message : " + exception.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException exception,
                                            Cache cache,
                                            Object key,
                                            Object value) {
                System.err.println("Cache put error: " + "Key : " + key + " Value : " + value + " Message : " + exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception,
                                              Cache cache,
                                              Object key) {
                System.err.println("Cache evict error: " + "Key : " + key + " Message : " + exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception,
                                              Cache cache) {
                System.err.println("Cache clear error: " + "Cache : " + cache.getName() + " Message : " + exception.getMessage());
            }
    }
