package com.learningSpringBoot.Stock.Trading.Portfolio.System.DTO;

import java.util.UUID;

public class StockRequest {

    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
