package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletResponse {

    private UUID uid;
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }
}
