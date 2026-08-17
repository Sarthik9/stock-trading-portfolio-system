package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.OrderResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Transactions;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.TransactionsEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.TransactionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionsService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionsService.class);

    public List<Transactions> getTransactionsByUserId(UUID userId) {

        List<TransactionsEntity> entity = transactionsRepository.findAllByUserId(userId);

        return mapToTransactions(entity);
    }

    private List<Transactions> mapToTransactions(List<TransactionsEntity> entity) {
        List<Transactions> transactions = new ArrayList<>();
        for(TransactionsEntity entityObj : entity) {
            Transactions transaction = new Transactions();
            transaction.setUserId(entityObj.getUserId());
            transaction.setTxnId(entityObj.getTxnId());
            transaction.setOrderId(OrderResponse.getOrderId());
            transaction.setStock(entityObj.getStockSymbol());
            transaction.setQuantity(entityObj.getQuantity());
            transaction.setPrice(entityObj.getPrice());
            transaction.setTransactionType(entityObj.getTransactionType());
            transaction.setTimestamp(entityObj.getTimestamp());
            transactions.add(transaction);
        }
        return transactions;
    }

    public void createTransaction(
             UUID userId,
             String stock,
             TransactionType type,
             BigDecimal amount,
             int quantity
    ){
        TransactionsEntity transactions = new TransactionsEntity();
        transactions.setUserId(userId);
        transactions.setTxnId(0);
        transactions.setOrderId(OrderResponse.getOrderId());
        transactions.setStockSymbol(stock);
        transactions.setPrice(amount);
        transactions.setTransactionType(type);
        transactions.setQuantity(quantity);
        transactions.setTimestamp(LocalDateTime.now());

        transactionsRepository.save(transactions);
        logger.info("New transaction created - txn : " + transactions.getTxnId() + " , type : " + transactions.getTransactionType());
    }
}
