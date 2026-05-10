package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import jakarta.validation.constraints.*;


import java.math.BigDecimal;
import java.util.UUID;

public class Order {

    @NotNull(message = "uid is required")
    private UUID uid;

    @NotBlank(message = "stock name is required")
    private String stock;

    @NotNull(message = "price is required")
    @Positive(message = "price should be greater than 0")
    private BigDecimal price;

    @NotNull(message = "quantity is required")
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
