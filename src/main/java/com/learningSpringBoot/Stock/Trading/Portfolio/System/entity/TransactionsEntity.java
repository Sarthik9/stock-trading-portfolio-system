package com.learningSpringBoot.Stock.Trading.Portfolio.System.entity;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
public class TransactionsEntity {

    @UuidGenerator
    @Id
    private String userId;
    private String stockSymbol;
    private int quantity;
    private double price;
    private TransactionType transactionType;  // "BUY" or "SELL"
    private LocalDateTime timestamp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
