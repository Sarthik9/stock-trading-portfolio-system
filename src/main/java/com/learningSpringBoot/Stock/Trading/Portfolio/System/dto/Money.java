package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class Money {

    @NotNull(message = "uid is required")
    private UUID uid;

    @Positive(message = "amount should be greater than 0")
    private BigDecimal money;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
