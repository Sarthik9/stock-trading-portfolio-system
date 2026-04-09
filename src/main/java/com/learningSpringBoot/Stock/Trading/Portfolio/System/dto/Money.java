package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import java.util.UUID;

public class Money {
    private UUID uid;
    private double money;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
