package com.learningSpringBoot.Stock.Trading.Portfolio.System.controller;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Money;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.WalletResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/getBalance/{userId}")
    public ResponseEntity<WalletResponse> getBalanceById(@PathVariable UUID userId){

        WalletResponse response = walletService.getWalletBalance(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addMoney")
    public ResponseEntity<WalletResponse> addMoney(@RequestBody Money money){
        WalletResponse response = walletService.addMoney(money);
        return ResponseEntity.ok(response);
    }
}
