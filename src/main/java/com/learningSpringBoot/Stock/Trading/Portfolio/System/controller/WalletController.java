package com.learningSpringBoot.Stock.Trading.Portfolio.System.controller;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Money;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.StockRequest;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.WalletResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    public ResponseEntity<WalletResponse> getBalanceById(@PathVariable UUID userId){

        StockRequest request = new StockRequest();
        request.setUserId(userId);

        WalletResponse response = walletService.getWalletBalance(request);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<WalletResponse> addMoney(@RequestBody Money money){
        WalletResponse response = walletService.addMoney(money);
        return ResponseEntity.ok(response);
    }
}
