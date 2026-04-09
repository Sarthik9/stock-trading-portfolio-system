package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.Order;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.OrderResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.StockRequest;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.StockResponse;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.StockEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public StockResponse getOrderDetails(StockRequest requestObj){
        StockEntity responseData = stockRepository.getById(requestObj.getUserId());

        return mapResponseDataToDTO(responseData);
    }

    private StockResponse mapResponseDataToDTO(StockEntity responseData) {

        StockResponse response = new StockResponse();
        response.setUserId(responseData.getuId());
        response.setStockSymbol(responseData.getStock());
        response.setPrice(responseData.getPrice());
        response.setQuantity(responseData.getQuantity());
        return response;
    }

    public OrderResponse createNewOrder(Order order) {

        StockEntity orderEntity = convertToOrderEntity(order);

        return convertToOrderResponse(stockRepository.save(orderEntity));
    }

    private StockEntity convertToOrderEntity(Order order) {
        StockEntity entity = new StockEntity();
        entity.setStock(order.getStock());
        entity.setuId(order.getuId());
        entity.setOrderType(order.getOrderType());
        entity.setQuantity(order.getQuantity());
        entity.setPrice(order.getPrice());
        return entity;
    }

    private OrderResponse convertToOrderResponse(StockEntity response) {
        OrderResponse result = new OrderResponse();

        result.setUid(response.getuId());
        result.setOrderId("AA");
        result.setStatus("SUCCESS");
        result.setOrderType(response.getOrderType());
        return result;
    }

    public List<StockResponse> fetchOrders() {
        List<StockEntity> ordersList = stockRepository.findAll();
        return ordersList.stream()
                .map(this::mapResponseDataToDTO)
                .toList();
    }
}
