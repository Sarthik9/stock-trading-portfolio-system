package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;

import java.util.UUID;

public class Order {

    private UUID uid;
    private long orderId;
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

    public UUID getuid() {
        return uid;
    }

    public void setuId(UUID uid) {
        this.uid = uid;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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
