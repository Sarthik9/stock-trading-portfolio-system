package com.learningSpringBoot.Stock.Trading.Portfolio.System.Entity;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.Models.OrderType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
public class StockEntity {

    @Id
    @UuidGenerator
    private UUID uId;
    private String stock;
    private long price;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public UUID getuId() {
        return uId;
    }

    public void setuId(UUID uId) {
        this.uId = uId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
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
