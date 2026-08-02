package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.TransactionsEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.TransactionsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionsService transactionsService;

    @Mock
    private TransactionsRepository transactionsRepository;

    private static final UUID uid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Test
    void shouldFindAllByUid() {
        TransactionsEntity transaction = new TransactionsEntity();
        transaction.setUserId(uid);
        transaction.setQuantity(5);
        transaction.setPrice(BigDecimal.valueOf(100));
        transaction.setStockSymbol("Apple");
        transaction.setTxnId(1);
        transaction.setTransactionType(TransactionType.BUY);
        transaction.setTimestamp(LocalDateTime.now());

        when(transactionsRepository.findAllByUserId(uid)).thenReturn(List.of(transaction));

        var response = transactionsService.getTransactionsByUserId(uid);

        assertEquals(1, response.get(0).getTxnId());
        assertEquals(uid, response.get(0).getUserId());
        assertEquals(5, response.get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(100), response.get(0).getPrice());
        assertEquals(TransactionType.BUY, response.get(0).getTransactionType());
    }
    @Test
    void shouldCreateTransaction() {

        transactionsService.createTransaction(
              uid,
              "Apple",
                TransactionType.BUY,
                BigDecimal.valueOf(800),
                2
        );

        verify(transactionsRepository).save(org.mockito.ArgumentMatchers.argThat( transactions ->
                transactions.getStockSymbol().equals("Apple") &&
                transactions.getTransactionType() == TransactionType.BUY &&
                transactions.getQuantity() == 2 &&
                transactions.getPrice().equals(BigDecimal.valueOf(800))
        ));
    }
}
