package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Portfolio;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.PortfolioEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.InsufficiencyException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.PortfolioNotFoundException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public List<Portfolio> getPortfolioByUserId(UUID userId) {
        List<PortfolioEntity> entity = portfolioRepository.findAllByUid(userId);

        return mapToPortfolio(entity);
    }

    public Portfolio getPortfolioByUserIdAndStock(UUID userId, String stock) {
        PortfolioEntity entity = portfolioRepository.findByUidAndStock(userId, stock)
                .orElseThrow( () -> new PortfolioNotFoundException( ("Portfolio not found for user with uid : " + userId + " , stock : " + stock)));

        return mapToPortfolio(Collections.singletonList(entity)).get(0);
    }

    private List<Portfolio> mapToPortfolio(List<PortfolioEntity> entity) {
        List<Portfolio> list = new ArrayList<>();
        for(PortfolioEntity item : entity) {
            Portfolio portfolio = new Portfolio();
            portfolio.setOrderId(item.getOrderId());
            portfolio.setStock(item.getStock());
            portfolio.setQuantity(item.getQuantity());
            portfolio.setAvgPrice(item.getAveragePrice());
            portfolio.setTotalInvestment(item.getTotalInvestment());
            list.add(portfolio);
        }
        return list;
    }

    public void updatePortfolio(
            UUID userId,
            String stock,
            OrderType orderType,
            int quantity,
            double avgPrice,
            double totalInvestment
    ){

        Optional<PortfolioEntity> optional = portfolioRepository.findByUidAndStock(userId, stock);
        if( orderType.equals( OrderType.BUY )) {

            PortfolioEntity entity;
            if ( optional.isPresent() ) {
                entity = optional.get();
                        // BUYING SAME STOCK, Increase quantity
                        entity.setQuantity(quantity + entity.getQuantity());
                        entity.setTotalInvestment(totalInvestment + quantity * avgPrice);
            } else {
                // BUYING A NEW STOCK
                entity = new PortfolioEntity();
                entity.setUid(userId);
                entity.setStock(stock);
                entity.setQuantity(quantity);
                entity.setAveragePrice(avgPrice);
                entity.setTotalInvestment(totalInvestment);
            }
            portfolioRepository.save(entity);
        } else {
            // SELLING EXISTING STOCK, reduce quantity
            PortfolioEntity portfolio = optional
                    .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found for user with uid : " + userId + " , stock : " + stock));

            if (quantity > portfolio.getQuantity())
                throw new InsufficiencyException("Not enough stocks to sell");

            int remainingQty = portfolio.getQuantity() - quantity;
            if (remainingQty == 0) {
                portfolioRepository.delete(portfolio);
            } else {
                portfolio.setQuantity(remainingQty);
                portfolio.setTotalInvestment(portfolio.getTotalInvestment() - portfolio.getAveragePrice() * quantity);
                portfolioRepository.save(portfolio);
            }
        }

        System.out.println("Updated Porfolio for user : " + userId);
    }
}