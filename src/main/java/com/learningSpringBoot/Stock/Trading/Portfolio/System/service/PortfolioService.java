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
}