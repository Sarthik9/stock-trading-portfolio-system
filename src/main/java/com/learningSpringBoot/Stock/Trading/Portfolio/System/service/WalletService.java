package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Money;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.WalletResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.WalletEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.InsufficiencyException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.WalletNotFoundException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.WalletRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionsService transactionsService;

    private static final String WALLET_NOT_FOUND = "Wallet not found for user : ";

    public WalletResponse getWalletBalance(UUID userId) {
        WalletEntity entity = walletRepository
                .findByUserId(userId)
                .orElseThrow(()-> new WalletNotFoundException(WALLET_NOT_FOUND + userId));
        return mapToWalletResponse(entity);
    }

    private WalletResponse mapToWalletResponse(WalletEntity entity) {
        WalletResponse response = new WalletResponse();
        response.setUid(entity.getUserId());
        response.setBalance(entity.getBalance());
        return response;
    }

    @Transactional
    public WalletResponse addMoney(Money money) {

        // check existing User and update balance accordingly
        // if not found create new wallet Entity
        WalletEntity entity = walletRepository
                .findByUserId(money.getUid())
                .orElseGet(() -> {
                    WalletEntity w = new WalletEntity();
                            w.setUserId(money.getUid());
                            w.setBalance(BigDecimal.ZERO);
                            return w;
                });

        entity.setBalance(entity.getBalance().add(money.getMoney()));

        // create Transaction
        transactionsService.createTransaction(money.getUid(), "", TransactionType.LOAD_WALLET, money.getMoney(), 0);

        return mapToWalletResponse(walletRepository.save(entity));
    }

    @Transactional
    public void debit(
            UUID userId,
            BigDecimal amount
    ){

        WalletEntity entity = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(WALLET_NOT_FOUND + userId));
        if (entity.getBalance().compareTo(amount) < 0){
            throw new InsufficiencyException("Not enough balance");
        }
        entity.setBalance(entity.getBalance().subtract(amount));
        walletRepository.save(entity);
    }

    @Transactional
    public void credit(UUID userId, BigDecimal amount) {
        WalletEntity entity = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(WALLET_NOT_FOUND + userId));
        entity.setBalance(entity.getBalance().add(amount));
        walletRepository.save(entity);
    }

    public void deleteWallet(@NotNull UUID userId) {
        WalletEntity entity = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(WALLET_NOT_FOUND + userId));
        walletRepository.delete(entity);
    }
}
