package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Money;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.WalletResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.WalletEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.InsufficiencyException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.WalletNotFoundException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionsService transactionsService;

    @Test
    void shouldCreateWalletSuccessfully(){

        Money money = new Money();
        money.setUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        money.setMoney(BigDecimal.valueOf(1000));

        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setUserId(money.getUid());
        walletEntity.setBalance(BigDecimal.valueOf(1000));

        when(walletRepository.findByUserId(money.getUid())).thenReturn(java.util.Optional.empty());

        when(walletRepository.save(any())).thenReturn(walletEntity);

        WalletResponse response = walletService.addMoney(money);
        assertNotNull(response);
        assertEquals(money.getUid(), response.getUid());
        assertEquals(money.getMoney(), response.getBalance());

        // Verify important calls
        verify(walletRepository).save(any(WalletEntity.class));

        verify(transactionsService).createTransaction(
                money.getUid(),
                "",
                TransactionType.LOAD_WALLET,
                money.getMoney(),
                0);
    }

    @Test
    void shouldUpdateBalanceIfUserAlreadyExists(){

        Money money = new Money();
        money.setUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        money.setMoney(BigDecimal.valueOf(500));

        WalletEntity existingEntity = new WalletEntity();
        existingEntity.setUserId(money.getUid());
        existingEntity.setBalance(BigDecimal.valueOf(1000));

        when(walletRepository.findByUserId(money.getUid())).thenReturn(java.util.Optional.of(existingEntity));

        WalletEntity updatedEntity = new WalletEntity();
        updatedEntity.setUserId(money.getUid());
        updatedEntity.setBalance(BigDecimal.valueOf(1500));

        when(walletRepository.save(any())).thenReturn(updatedEntity);

        WalletResponse response = walletService.addMoney(money);
        assertNotNull(response);
        assertEquals(money.getUid(), response.getUid());
        assertEquals(BigDecimal.valueOf(1500), response.getBalance());

        verify(walletRepository).save(any(WalletEntity.class));
        verify(transactionsService).createTransaction(
                money.getUid(),
                "",
                TransactionType.LOAD_WALLET,
                money.getMoney(),
                0);

    }

    @Test
    void shouldGetBalance(){

        UUID uid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        WalletEntity existingEntity = new WalletEntity();
        existingEntity.setUserId(uid);
        existingEntity.setBalance(BigDecimal.valueOf(1000));

        when(walletRepository.findByUserId(uid)).thenReturn(java.util.Optional.of(existingEntity));

        WalletResponse response = walletService.getWalletBalance(uid);
        assertNotNull(response);
        assertEquals(uid, response.getUid());
        assertEquals(BigDecimal.valueOf(1000), response.getBalance());

    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {

        UUID uid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        when(walletRepository.findByUserId(uid)).thenReturn(java.util.Optional.empty());

        WalletNotFoundException ex = assertThrows(WalletNotFoundException.class, () -> walletService.getWalletBalance(uid));
        assertTrue(ex.getMessage().contains("Wallet not found for user : " + uid));

    }

    @Test
    void shouldDebitMoneySuccessfully(){

        UUID uid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        BigDecimal amount = BigDecimal.valueOf(500);

        WalletEntity existingEntity = new WalletEntity();
        existingEntity.setUserId(uid);
        existingEntity.setBalance(BigDecimal.valueOf(1000));

        when(walletRepository.findByUserId(uid)).thenReturn(java.util.Optional.of(existingEntity));

        walletService.debit(uid, amount);

        assertEquals(BigDecimal.valueOf(500), existingEntity.getBalance());

        verify(walletRepository).save(existingEntity);

    }

    @Test
    void shouldThrowExceptionIfInsufficientBalance() {

        UUID uid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        BigDecimal amount = BigDecimal.valueOf(1500);

        WalletEntity existingEntity = new WalletEntity();
        existingEntity.setUserId(uid);
        existingEntity.setBalance(BigDecimal.valueOf(1000));

        when(walletRepository.findByUserId(uid)).thenReturn(java.util.Optional.of(existingEntity));

        InsufficiencyException ex = assertThrows(InsufficiencyException.class, () -> walletService.debit(uid, amount));
        assertTrue(ex.getMessage().contains("Not enough balance"));

    }

    @Test
    void shouldCreditMoneySuccessfully(){

        UUID uid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        BigDecimal amount = BigDecimal.valueOf(500);

        WalletEntity existingEntity = new WalletEntity();
        existingEntity.setUserId(uid);
        existingEntity.setBalance(BigDecimal.valueOf(1000));

        when(walletRepository.findByUserId(uid)).thenReturn(java.util.Optional.of(existingEntity));

        walletService.credit(uid, amount);

        assertEquals(BigDecimal.valueOf(1500), existingEntity.getBalance());

        verify(walletRepository).save(existingEntity);

    }

}
