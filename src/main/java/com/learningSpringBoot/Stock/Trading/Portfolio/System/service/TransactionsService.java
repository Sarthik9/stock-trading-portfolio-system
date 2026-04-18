package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Transactions;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.TransactionsEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionsService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    public Transactions getTransactionsByUserId(UUID userId) {

        TransactionsEntity entity = transactionsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Transactions not found for userId: " + userId));

        return mapToTransactions(entity);
    }

    private Transactions mapToTransactions(TransactionsEntity entity) {
        Transactions transactions = new Transactions();
        transactions.setStock(entity.getStockSymbol());
        transactions.setQuantity(entity.getQuantity());
        transactions.setPrice(entity.getPrice());
        transactions.setTransactionType(entity.getTransactionType());
        transactions.setTimestamp(entity.getTimestamp());
        return transactions;
    }

    public void createTransaction(
             UUID userId,
             String stock,
             TransactionType type,
             double amount,
             int quantity
    ){
        Transactions transactions = new Transactions();
        transactions.setUserId(userId);
        transactions.setStock(stock);
        transactions.setPrice(amount);
        transactions.setTransactionType(type);
        transactions.setQuantity(quantity);
        transactions.setTimestamp(LocalDateTime.now());
    }
}
