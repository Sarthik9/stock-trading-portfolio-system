package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.*;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.StockEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.InsufficiencyException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TransactionsService transactionsService;

    public List<StockResponse> getOrderDetails(StockRequest requestObj){
        List<StockEntity> responseData = stockRepository.findAllByUid(requestObj.getUserId());

        return mapResponseDataToDTO(responseData);
    }

    private List<StockResponse> mapResponseDataToDTO(List<StockEntity> responseData) {

        List<StockResponse> orders = new ArrayList<>();
        for(StockEntity entity : responseData) {
            StockResponse response = new StockResponse();
            response.setUserId(entity.getuid());
            response.setOrderId(entity.getOrderId());
            response.setStockSymbol(entity.getStock());
            response.setOrderType(entity.getOrderType());
            response.setPrice(entity.getPrice());
            response.setQuantity(entity.getQuantity());
            orders.add(response);
        }
        return orders;
    }

    //Orders are historical records
    @Transactional
    public OrderResponse createNewOrder(Order order) {

        // OrderType - BUY
        if(order.getOrderType().equals(OrderType.BUY)) {

            // check if user has balance or not
            WalletResponse currentBalance = walletService.getWalletBalance(order.getuid());

            long calculatedOrderPrice = order.getPrice() * order.getQuantity();

        if(currentBalance.getBalance() >= calculatedOrderPrice){

            // Debit wallet
            walletService.debit(order.getuid(), calculatedOrderPrice);

            // Update Portfolio
            portfolioService.updatePortfolio(order.getuid(), order.getStock(), order.getOrderType(), order.getQuantity(),
                    order.getPrice(), calculatedOrderPrice);

            // Create transaction
            transactionsService.createTransaction(order.getuid(), order.getStock(), TransactionType.BUY, calculatedOrderPrice,
                    order.getQuantity());

        }

        else throw new InsufficiencyException("Not Enough Balance");

        }

        else {

            // Ordertype - SELL
            Portfolio stockHoldings = portfolioService.getPortfolioByUserIdAndStock(order.getuid(), order.getStock());
            int quantityOfStocks = stockHoldings.getQuantity();
            int quantityOfStocksToSell = order.getQuantity();

            if (quantityOfStocksToSell > quantityOfStocks)
                throw new InsufficiencyException("Not enough stocks to sell !");

            // Update Portfolio
            portfolioService.updatePortfolio(order.getuid(), order.getStock(), order.getOrderType(), order.getQuantity(), 0.0, 0.0);
            System.out.println("Sold successfully - stocks : " + order.getStock() + " , quantity : " + order.getQuantity());

            // Credit Wallet
            walletService.credit(order.getuid(), quantityOfStocksToSell*stockHoldings.getAvgPrice());

            // Create transaction
            transactionsService.createTransaction(order.getuid(), order.getStock(), TransactionType.SELL,
                    quantityOfStocksToSell*stockHoldings.getAvgPrice(), order.getQuantity());
        }

        StockEntity orderEntity = convertToOrderEntity(order);
        return convertToOrderResponse(stockRepository.save(orderEntity));
    }

    private StockEntity convertToOrderEntity(Order order) {

        StockEntity entity = new StockEntity();
            // New user and order
            entity.setStock(order.getStock());
            entity.setuid(order.getuid());
            entity.setOrderType(order.getOrderType());
            entity.setQuantity(order.getQuantity());
            entity.setPrice(order.getPrice());

        return entity;
    }

    private OrderResponse convertToOrderResponse(StockEntity response) {
        OrderResponse result = new OrderResponse();

        result.setUid(response.getuid());
        result.setOrderId(response.getOrderId());
        result.setStatus("SUCCESS");
        result.setOrderType(response.getOrderType());
        return result;
    }

    public List<StockResponse> fetchOrders() {
        List<StockEntity> ordersList = stockRepository.findAll();
        return mapResponseDataToDTO(ordersList);
    }
}
