package com.learningSpringBoot.Stock.Trading.Portfolio.System.Controller;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.DTO.Order;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.DTO.OrderResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.DTO.StockRequest;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.DTO.StockResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.Service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/Orders")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{userId}")
    public ResponseEntity<StockResponse> getOrderByUserId(@PathVariable UUID userId){

        StockRequest requestObj = new StockRequest();
        requestObj.setUserId(userId);

        StockResponse response = stockService.getOrderDetails(requestObj);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody Order order){

        OrderResponse response = stockService.createNewOrder(order);
        return ResponseEntity.ok(response);
    }
}

