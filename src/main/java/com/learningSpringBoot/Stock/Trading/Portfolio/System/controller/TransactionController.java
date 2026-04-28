package com.learningSpringBoot.Stock.Trading.Portfolio.System.controller;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Transactions;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionsService transactionsService;

    @GetMapping("/getTransactions")
    public ResponseEntity<List<Transactions>> getTransactionsByUserId(@RequestParam UUID userId){

        List<Transactions> response = transactionsService.getTransactionsByUserId(userId);

        return ResponseEntity.ok(response);
    }
}
