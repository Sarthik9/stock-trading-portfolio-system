package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Order;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.OrderResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.StockResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.WalletResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.StockEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.WalletEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.StockRepository;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
