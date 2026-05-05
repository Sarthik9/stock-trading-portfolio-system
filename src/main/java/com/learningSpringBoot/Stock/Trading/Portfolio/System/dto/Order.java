package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.math.BigDecimal;
import java.util.UUID;

public class Order {

    @NotNull(message = "uid is required")
    private UUID uid;
    private long orderId;

    @NotNull(message = "stock name is reuired")
    private String stock;

    @Positive(message = "price should be greater than 0")
    private BigDecimal price;

    @Min(message = "minimum quantity is 1", value = 1)
    private int quantity;

    @NotNull(message = "Order type should be Buy or Sell")
    private OrderType orderType;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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
