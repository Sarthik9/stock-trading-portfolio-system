package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Portfolio;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.PortfolioEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.InsufficiencyException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.PortfolioNotFoundException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class PortfolioService {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioService.class);

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Cacheable(value = "portfolioCache", key = "#userId")
    public List<Portfolio> getPortfolioByUserId(UUID userId) {
        List<PortfolioEntity> entity = portfolioRepository.findAllByUid(userId);

        System.out.println("In PortfolioService DB HIT");
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
            portfolio.setStock(item.getStock());
            portfolio.setQuantity(item.getQuantity());
            portfolio.setAvgPrice(item.getAveragePrice());
            portfolio.setTotalInvestment(item.getTotalInvestment());
            list.add(portfolio);
        }
        return list;
    }

    @CacheEvict(value = "portfolioCache", key = "#userId")
    public void updatePortfolio(
            UUID userId,
            String stock,
            OrderType orderType,
            int quantity,
            BigDecimal price,
            BigDecimal orderInvestment
    ){

        Optional<PortfolioEntity> optional = portfolioRepository.findByUidAndStock(userId, stock);
        if( orderType.equals( OrderType.BUY )) {

            PortfolioEntity entity;
            if ( optional.isPresent() ) {
                entity = optional.get();
                        // BUYING SAME STOCK, Increase quantity
                        BigDecimal newTotal = entity.getTotalInvestment().add(orderInvestment);
                        int newQty = entity.getQuantity() + quantity;
                        BigDecimal newAvg = newTotal.divide(BigDecimal.valueOf(newQty), 1, RoundingMode.HALF_UP);
                        entity.setQuantity(newQty);
                        entity.setAveragePrice(newAvg);
                        entity.setTotalInvestment(newTotal);
            } else {
                // BUYING A NEW STOCK
                entity = new PortfolioEntity();
                entity.setUid(userId);
                entity.setStock(stock);
                entity.setQuantity(quantity);
                entity.setAveragePrice(price);
                entity.setTotalInvestment(orderInvestment);
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
                portfolio.setTotalInvestment(portfolio.getTotalInvestment().subtract(portfolio.getAveragePrice().multiply(BigDecimal.valueOf(quantity))));
                portfolioRepository.save(portfolio);
            }
        }

        logger.info("Updated Porfolio for user : " + userId);
    }
}