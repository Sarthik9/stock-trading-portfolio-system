package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.*;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.StockEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.InsufficiencyException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private WalletService walletService;

    @Mock
    private PortfolioService portfolioService;

    @Mock
    private TransactionsService transactionsService;

    private static final UUID uid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Test
    void shouldGetOrderDetailsSuccessfully() {

        StockEntity stockEntity = new StockEntity();
        stockEntity.setuid(uid);
        stockEntity.setOrderId(1);
        stockEntity.setStock("Apple");
        stockEntity.setOrderType(OrderType.BUY);
        stockEntity.setPrice(BigDecimal.valueOf(500));
        stockEntity.setQuantity(2);

        when(stockRepository.findAllByUid(uid)).thenReturn(java.util.List.of(stockEntity));

        StockRequest req = new StockRequest();
        req.setUserId(uid);
        var response = stockService.getOrderDetails(req);
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(uid, response.get(0).getUserId());
        assertEquals("Apple", response.get(0).getStockSymbol());
        assertEquals(OrderType.BUY, response.get(0).getOrderType());
        assertEquals(BigDecimal.valueOf(500), response.get(0).getPrice());
        assertEquals(2, response.get(0).getQuantity());

        verify(stockRepository).findAllByUid(uid);
    }

    @Test
    void shouldCreateOrderTypeBuySuccessfully(){

        Order order = new Order();
        order.setuId(uid);
        order.setStock("Apple");
        order.setOrderType(OrderType.BUY);
        order.setQuantity(2);
        order.setPrice(BigDecimal.valueOf(500));

        WalletResponse response = new WalletResponse();
        response.setUid(uid);
        response.setBalance(BigDecimal.valueOf(2000));

        when(walletService.getWalletBalance(uid)).thenReturn(response);

        when(stockRepository.save(any())).thenReturn(new StockEntity());

        OrderResponse response1 = stockService.createNewOrder(order);

        assertNotNull(response);
        assertEquals(response.getUid(), order.getuid());
        assertEquals( "SUCCESS", response1.getStatus());

        verify(stockRepository).save(any(StockEntity.class));
        verify(walletService).getWalletBalance(uid);
        verify(portfolioService).updatePortfolio(
                uid,
                "Apple",
                OrderType.BUY,
                2,
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(1000)
                );
        verify(transactionsService).createTransaction(
                uid,
                "Apple",
                TransactionType.BUY,
                order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())),
                order.getQuantity());

    }

    @Test
    void shouldThrowInsufficientBalanceForBuyOrder() {

        Order order = new Order();
        order.setuId(uid);
        order.setStock("Apple");
        order.setOrderType(OrderType.BUY);
        order.setQuantity(2);
        order.setPrice(BigDecimal.valueOf(500));

        WalletResponse response = new WalletResponse();
        response.setUid(uid);
        response.setBalance(BigDecimal.valueOf(500));

        when(walletService.getWalletBalance(uid)).thenReturn(response);

        InsufficiencyException exception = assertThrows(
                InsufficiencyException.class,
                () -> stockService.createNewOrder(order)
        );
        assertEquals("Not Enough Balance", exception.getMessage());

        verify(walletService).getWalletBalance(uid);
        verify(stockRepository, never()).save(any(StockEntity.class));
        verify(walletService, never()).debit(any(), any());
        verify(portfolioService, never()).updatePortfolio(
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any()
        );
        verify(transactionsService, never()).createTransaction(any(), any(), any(), any(), anyInt());
    }

    @Test
    void shouldCreateOrderTypeSellSuccessfully(){

        Order order = new Order();
        order.setuId(uid);
        order.setStock("Apple");
        order.setOrderType(OrderType.SELL);
        order.setQuantity(2);
        order.setPrice(BigDecimal.valueOf(500));

        when(stockRepository.save(any())).thenReturn(new StockEntity());

        Portfolio portfolio = new Portfolio();
        portfolio.setOrderId(1);
        portfolio.setStock("Apple");
        portfolio.setQuantity(5);
        portfolio.setAvgPrice(BigDecimal.valueOf(400));
        portfolio.setTotalInvestment(BigDecimal.valueOf(2000));

        when(portfolioService.getPortfolioByUserIdAndStock(uid, "Apple")).thenReturn(portfolio);

        OrderResponse response1 = stockService.createNewOrder(order);

        assertNotNull(response1);
        assertEquals( "SUCCESS", response1.getStatus());

        verify(stockRepository).save(any(StockEntity.class));

        verify(portfolioService).getPortfolioByUserIdAndStock(uid, "Apple");

        verify(portfolioService).updatePortfolio(
                uid,
                "Apple",
                OrderType.SELL,
                2,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        verify(walletService).credit(
                uid,
                portfolio.getAvgPrice().multiply(BigDecimal.valueOf(order.getQuantity()))
        );

        verify(transactionsService).createTransaction(
                uid,
                "Apple",
                TransactionType.SELL,
                portfolio.getAvgPrice().multiply(BigDecimal.valueOf(order.getQuantity())),
                order.getQuantity());
    }

    @Test
    void shouldThrowInsufficientStocksForSellOrder() {

        Order order = new Order();
        order.setuId(uid);
        order.setStock("Apple");
        order.setOrderType(OrderType.SELL);
        order.setQuantity(5);
        order.setPrice(BigDecimal.valueOf(500));

        Portfolio portfolio = new Portfolio();
        portfolio.setOrderId(1);
        portfolio.setStock("Apple");
        portfolio.setQuantity(2);
        portfolio.setAvgPrice(BigDecimal.valueOf(400));
        portfolio.setTotalInvestment(BigDecimal.valueOf(800));

        when(portfolioService.getPortfolioByUserIdAndStock(uid, "Apple")).thenReturn(portfolio);

        InsufficiencyException exception = assertThrows(
                InsufficiencyException.class,
                () -> stockService.createNewOrder(order)
        );
        assertEquals("Not enough stocks to sell !", exception.getMessage());

        verify(portfolioService).getPortfolioByUserIdAndStock(uid, "Apple");
        verify(stockRepository, never()).save(any(StockEntity.class));
        verify(portfolioService, never()).updatePortfolio(
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any()
        );
        verify(walletService, never()).credit(any(), any());
        verify(transactionsService, never()).createTransaction(any(), any(), any(), any(), anyInt());
    }

}
