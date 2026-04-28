package com.learningSpringBoot.Stock.Trading.Portfolio.System.controller;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Portfolio;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/getPortfolio")
    public ResponseEntity<List<Portfolio>> getPortfolio(@RequestParam UUID userId){

        List<Portfolio> response = portfolioService.getPortfolioByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
