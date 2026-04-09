package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import java.util.UUID;

public class WalletResponse {

    private UUID uid;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
