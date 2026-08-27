package com.learningSpringBoot.Stock.Trading.Portfolio.System.controller;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Money;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.WalletResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.WalletService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/getBalance/{userId}")
    public ResponseEntity<WalletResponse> getBalanceById(@PathVariable @NotNull UUID userId){

        WalletResponse response = walletService.getWalletBalance(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addMoney")
    public ResponseEntity<WalletResponse> addMoney(@RequestBody @Valid Money money){
        WalletResponse response = walletService.addMoney(money);
        return ResponseEntity.created(null).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteWallet/{userId}")
    public ResponseEntity<String> deleteWallet(@PathVariable @NotNull UUID userId){

        walletService.deleteWallet(userId);
        return ResponseEntity.ok("Wallet deleted successfully");
    }
}
