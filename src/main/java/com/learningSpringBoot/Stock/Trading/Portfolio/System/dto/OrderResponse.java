package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;

import java.util.UUID;

public class OrderResponse {

    private UUID uid;
    private long orderId;
    private String status;
    private OrderType orderType;

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getOrderId() {
        return orderId;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }
}
