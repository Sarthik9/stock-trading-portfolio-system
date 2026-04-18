package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Portfolio;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.PortfolioEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Portfolio getPortfolioByUserId(UUID userId) {
        PortfolioEntity entity = portfolioRepository.findByUid(userId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found for userId: " + userId));

        return mapToPortfolio(entity);
    }

    private Portfolio mapToPortfolio(PortfolioEntity entity) {
        Portfolio portfolio = new Portfolio();
        portfolio.setStock(entity.getStock());
        portfolio.setQuantity(entity.getQuantity());
        portfolio.setAvgPrice(entity.getAveragePrice());
        portfolio.setTotalInvestment(entity.getTotalInvestment());
        return portfolio;
    }

    public void updatePortfolio(
            UUID userId,
            String stock,
            int quantity,
            double avgPrice,
            double totalInvestment
    ){
        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setUid(userId);
        portfolio.setStock(stock);
        portfolio.setQuantity(quantity);
        portfolio.setAveragePrice(avgPrice);
        portfolio.setTotalInvestment(totalInvestment);

        portfolioRepository.save(portfolio);
        System.out.println("Updated Porfolio for user : " + userId);
    }

    public void deletePortfolio(
            UUID userId,
            String stock
    ){
        PortfolioEntity entity = portfolioRepository.findByUidAndStock(userId, stock)
                .orElseThrow(() -> new RuntimeException("Cannot find stock - " + stock + " in Portfolio"));

        portfolioRepository.delete(entity);
    }
}