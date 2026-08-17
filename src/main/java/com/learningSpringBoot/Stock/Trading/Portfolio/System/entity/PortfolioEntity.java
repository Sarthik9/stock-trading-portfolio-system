package com.learningSpringBoot.Stock.Trading.Portfolio.System.entity;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.PortfolioId;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "portfolio", indexes = @Index(name = "idx_portfolio_uid_stock", columnList = "uid, stock"))
@IdClass(PortfolioId.class)
public class PortfolioEntity {

    @Id
    private UUID uid;

    @Id
    private String stock;

    private int quantity;

    @Column(precision = 15, scale = 2)
    private BigDecimal averagePrice;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalInvestment;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(BigDecimal totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
}
