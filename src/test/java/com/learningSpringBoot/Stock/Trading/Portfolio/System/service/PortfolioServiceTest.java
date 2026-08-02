package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.PortfolioEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.InsufficiencyException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.PortfolioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioService portfolioService;

    private static final UUID uid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Test
    void shouldGetPortfolioSuccessfully() {

        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setUid(uid);
        portfolio.setOrderId(1);
        portfolio.setStock("Apple");
        portfolio.setQuantity(2);
        portfolio.setAveragePrice(BigDecimal.valueOf(800));
        portfolio.setTotalInvestment(BigDecimal.valueOf(1600));

        when(portfolioRepository.findAllByUid(uid)).thenReturn(List.of(portfolio));

        var response = portfolioService.getPortfolioByUserId(uid);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(portfolio.getOrderId(), response.get(0).getOrderId());
        assertEquals(portfolio.getStock(), response.get(0).getStock());
        assertEquals(portfolio.getQuantity(), response.get(0).getQuantity());
        assertEquals(portfolio.getAveragePrice(), response.get(0).getAvgPrice());
        assertEquals(portfolio.getTotalInvestment(), response.get(0).getTotalInvestment());

        verify(portfolioRepository).findAllByUid(uid);
    }

    @Test
    void shouldGetPortfolioByUserIdAndStock() {

        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setUid(uid);
        portfolio.setOrderId(1);
        portfolio.setStock("Apple");
        portfolio.setQuantity(2);
        portfolio.setAveragePrice(BigDecimal.valueOf(800));
        portfolio.setTotalInvestment(BigDecimal.valueOf(1600));

        when(portfolioRepository.findByUidAndStock(uid, "Apple")).thenReturn(Optional.of(portfolio));

        var response = portfolioService.getPortfolioByUserIdAndStock(uid, "Apple");

        assertEquals(portfolio.getStock(), response.getStock());
        assertEquals(portfolio.getQuantity(), response.getQuantity());

        verify(portfolioRepository).findByUidAndStock(uid, "Apple");
    }

    @Test
    void shouldIncreaseQuantityAfterBuyingSameStock() {

        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setUid(uid);
        portfolio.setOrderId(1);
        portfolio.setStock("Apple");
        portfolio.setQuantity(2);
        portfolio.setAveragePrice(BigDecimal.valueOf(800));
        portfolio.setTotalInvestment(BigDecimal.valueOf(1600));

        when(portfolioRepository.findByUidAndStock(uid, "Apple")).thenReturn(Optional.of(portfolio));

        portfolioService.updatePortfolio(
                uid,
                "Apple",
                OrderType.BUY,
                3,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(3000)
        );

        assertEquals(5, portfolio.getQuantity());
        assertEquals(BigDecimal.valueOf(920.0), portfolio.getAveragePrice());
        assertEquals(BigDecimal.valueOf(4600), portfolio.getTotalInvestment());

        verify(portfolioRepository).save(portfolio);
    }

    @Test
    void shouldDecreaseQuantityAfterSellingStock() {

        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setUid(uid);
        portfolio.setOrderId(1);
        portfolio.setStock("Apple");
        portfolio.setQuantity(5);
        portfolio.setAveragePrice(BigDecimal.valueOf(800));
        portfolio.setTotalInvestment(BigDecimal.valueOf(4000));

        when(portfolioRepository.findByUidAndStock(uid, "Apple")).thenReturn(Optional.of(portfolio));

        portfolioService.updatePortfolio(
                uid,
                "Apple",
                OrderType.SELL,
                3,
                BigDecimal.valueOf(800),
                BigDecimal.valueOf(2400)
        );

        assertEquals(2, portfolio.getQuantity());
        assertEquals(BigDecimal.valueOf(800), portfolio.getAveragePrice());
        assertEquals(BigDecimal.valueOf(1600), portfolio.getTotalInvestment());

        verify(portfolioRepository).save(portfolio);
    }

    @Test
    void shouldCreateNewPortfolioAfterBuyingNewStock() {

        when(portfolioRepository.findByUidAndStock(uid, "Microsoft")).thenReturn(Optional.empty());

        portfolioService.updatePortfolio(
                uid,
                "Microsoft",
                OrderType.BUY,
                4,
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(2000)
        );

        verify(portfolioRepository).save(org.mockito.ArgumentMatchers.argThat(portfolio ->
            portfolio.getUid().equals(uid) &&
            portfolio.getStock().equals("Microsoft") &&
            portfolio.getQuantity() == 4 &&
            portfolio.getAveragePrice().equals(BigDecimal.valueOf(500)) &&
            portfolio.getTotalInvestment().equals(BigDecimal.valueOf(2000))
        ));
    }

    @Test
    void shouldThrowInsufficiencyExceptionWhenSellingStockIsNotEnough() {
        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setUid(uid);
        portfolio.setOrderId(1);
        portfolio.setStock("Microsoft");
        portfolio.setQuantity(2);
        portfolio.setAveragePrice(BigDecimal.valueOf(800));
        portfolio.setTotalInvestment(BigDecimal.valueOf(1600));
        when(portfolioRepository.findByUidAndStock(uid, "Microsoft")).thenReturn(Optional.of(portfolio));

        InsufficiencyException excpetion = assertThrows(
                InsufficiencyException.class,
                () -> portfolioService.updatePortfolio(
                uid,
                "Microsoft",
                OrderType.SELL,
                3,
                BigDecimal.valueOf(800),
                BigDecimal.valueOf(2400)
        )
        );

        assertEquals("Not enough stocks to sell", excpetion.getMessage());
    }
}
