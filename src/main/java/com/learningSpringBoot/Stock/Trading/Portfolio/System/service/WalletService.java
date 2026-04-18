package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Money;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.WalletResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.WalletEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.TransactionsRepository;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionsService transactionsService;

    public WalletResponse getWalletBalance(UUID userId) {
        WalletEntity entity = walletRepository
                .findByUserId(userId)
                .orElseThrow(()-> new RuntimeException("Wallet not found"));
        return mapToWalletResponse(entity);
    }

    private WalletResponse mapToWalletResponse(WalletEntity entity) {
        WalletResponse response = new WalletResponse();
        response.setUid(entity.getUserId());
        response.setBalance(entity.getBalance());
        return response;
    }

    public WalletResponse addMoney(Money money) {

        // check existing User and update balance accordingly
        // if not found create new wallet Entity
        WalletEntity entity = walletRepository
                .findByUserId(money.getUid())
                .orElse(new WalletEntity());

        entity.setUserId(money.getUid());

        entity.setBalance(entity.getBalance() + money.getMoney());

        // create Transaction
        transactionsService.createTransaction(money.getUid(), null, null, TransactionType.LOAD_WALLET, money.getMoney(), 0);

        return mapToWalletResponse(walletRepository.save(entity));
    }

    public void debit(
            UUID userId,
            double amount
    ){

        WalletEntity entity = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user : " + userId));
        entity.setBalance(entity.getBalance() - amount);
        walletRepository.save(entity);
    }

    public void credit(UUID userId, double amount) {
        WalletEntity entity = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user : " + userId));
        entity.setBalance(entity.getBalance() + amount);
        walletRepository.save(entity);
    }
}
