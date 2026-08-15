package com.learningSpringBoot.Stock.Trading.Portfolio.System.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class PortfolioId implements Serializable {

    private UUID uid;
    private String stock;

    public PortfolioId() {}

    public PortfolioId(UUID uid, String stock) {
        this.uid = uid;
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortfolioId)) return false;
        PortfolioId that = (PortfolioId) o;
        return Objects.equals(uid, that.uid) &&
                Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, stock);
    }
}
