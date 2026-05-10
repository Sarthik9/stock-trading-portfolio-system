package com.learningSpringBoot.Stock.Trading.Portfolio.System.controller;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Order;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.OrderResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.StockRequest;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.StockResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.service.StockService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<StockResponse>> getOrderByUserId(@PathVariable @NotNull UUID userId){

        StockRequest requestObj = new StockRequest();
        requestObj.setUserId(userId);

        List<StockResponse> response = stockService.getOrderDetails(requestObj);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetchOrders")
    public ResponseEntity<List<StockResponse>> fetchAllOrders(){

        List<StockResponse> responseList = stockService.fetchOrders();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid Order order){

        OrderResponse response = stockService.createNewOrder(order);
        URI location = URI.create("/orders/" + response.getOrderId());
        return ResponseEntity.created(location).body(response);
    }
}

