package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.*;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.StockEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.InsufficiencyException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.exception.ServiceUnavailableException;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private IdempotencyService idempotencyService;

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

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
    public OrderResponse createNewOrder(Order order, String idempotencyKey) {

        try {
            // 1. check redis idempotency
            OrderResponse existingResponse = idempotencyService.getExistingResponse(order.getuid(), idempotencyKey);
            if (existingResponse != null) {
                logger.info("Returning already existing order from Redis for userId: " + order.getuid() + " and idempotencyKey: " + idempotencyKey);
                return existingResponse;
            }
        } catch (RedisConnectionFailureException e) {
                logger.warn(
                        "Redis unavailable. Continuing with database idempotency."
                );
            }

        // 2. database idempotency check
        Optional<StockEntity> existingOrder =
                stockRepository.findByUidAndIdempotencyKey(
                        order.getuid(),
                        idempotencyKey
                );

        if (existingOrder.isPresent()) {
            logger.info("Returning already existing order in database for userId: " + order.getuid() + " and idempotencyKey: " + idempotencyKey);
            return convertToOrderResponse(existingOrder.get());
        }

            // 2. Buy/Sell logic
            // OrderType - BUY
            if (order.getOrderType().equals(OrderType.BUY)) {

                // check if user has balance or not
                WalletResponse currentBalance = walletService.getWalletBalance(order.getuid());

                BigDecimal calculatedOrderPrice = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));

                // balance > orderPrice
                if (currentBalance.getBalance().compareTo(calculatedOrderPrice) > 0) {

                    // Debit wallet
                    walletService.debit(order.getuid(), calculatedOrderPrice);

                    // Update Portfolio
                    portfolioService.updatePortfolio(order.getuid(), order.getStock(), order.getOrderType(), order.getQuantity(),
                            order.getPrice(), calculatedOrderPrice);

                    // Create transaction
                    transactionsService.createTransaction(order.getuid(), order.getStock(), TransactionType.BUY, calculatedOrderPrice,
                            order.getQuantity());

                } else throw new InsufficiencyException("Not Enough Balance");

            } else {

                // Ordertype - SELL
                Portfolio stockHoldings = portfolioService.getPortfolioByUserIdAndStock(order.getuid(), order.getStock());
                int quantityOfStocks = stockHoldings.getQuantity();
                int quantityOfStocksToSell = order.getQuantity();

                if (quantityOfStocksToSell > quantityOfStocks)
                    throw new InsufficiencyException("Not enough stocks to sell !");

                // Update Portfolio
                portfolioService.updatePortfolio(order.getuid(), order.getStock(), order.getOrderType(), order.getQuantity(), new BigDecimal("0"), new BigDecimal("0"));
                logger.info("Sold successfully - stocks : " + order.getStock() + " , quantity : " + order.getQuantity());

                // Credit Wallet
                walletService.credit(order.getuid(), stockHoldings.getAvgPrice().multiply(BigDecimal.valueOf(quantityOfStocksToSell)));

                // Create transaction
                transactionsService.createTransaction(order.getuid(), order.getStock(), TransactionType.SELL,
                        stockHoldings.getAvgPrice().multiply(BigDecimal.valueOf(quantityOfStocksToSell)), order.getQuantity());
            }

            StockEntity orderEntity = convertToOrderEntity(order, idempotencyKey);

            // 3. save result
            OrderResponse response = convertToOrderResponse(stockRepository.save(orderEntity));

        // 4. Try to cache response in Redis
        try {

            idempotencyService.saveResponse(
                    order.getuid(),
                    idempotencyKey,
                    response
            );

        } catch (RedisConnectionFailureException e) {

            logger.warn(
                    "Redis unavailable while saving idempotency response. " +
                            "Order was created successfully."
            );
        }

            return response;
    }


    private StockEntity convertToOrderEntity(Order order, String idempotencyKey) {

        StockEntity entity = new StockEntity();
            // New user and order
            entity.setStock(order.getStock());
            entity.setuid(order.getuid());
            entity.setOrderType(order.getOrderType());
            entity.setQuantity(order.getQuantity());
            entity.setPrice(order.getPrice());
            entity.setIdempotencyKey(idempotencyKey);

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
