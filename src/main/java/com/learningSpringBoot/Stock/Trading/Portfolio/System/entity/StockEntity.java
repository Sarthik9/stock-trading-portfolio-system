package com.learningSpringBoot.Stock.Trading.Portfolio.System.entity;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "stock_orders", indexes = @Index(name = "idx_stock_orders_uid", columnList = "uid"))
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    private String stock;
    private UUID uid;

    @Column(precision = 15, scale = 2)
    private BigDecimal price;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public UUID getuid() {
        return uid;
    }

    public void setuid(UUID uid) {
        this.uid = uid;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}
