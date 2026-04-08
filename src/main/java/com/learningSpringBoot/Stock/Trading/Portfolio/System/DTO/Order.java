package com.learningSpringBoot.Stock.Trading.Portfolio.System.DTO;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.Models.OrderType;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

public class Order {

    private UUID uId;
    private String stock;
    private long price;
    private int quantity;
    private OrderType orderType;

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public UUID getuId() {
        return uId;
    }

    public void setuId(UUID uId) {
        this.uId = uId;
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

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}
