package com.learningSpringBoot.Stock.Trading.Portfolio.System.service;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.dto.*;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.StockEntity;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.OrderType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.model.TransactionType;
import com.learningSpringBoot.Stock.Trading.Portfolio.System.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        // OrderType - BUY
        // check if user has balance or not
        WalletResponse currentBalance = walletService.getWalletBalance(order.getuId());
        if(order.getOrderType().equals(OrderType.BUY)) {

            long calculatedOrderPrice = order.getPrice() * order.getQuantity();

        if(currentBalance.getBalance() >= calculatedOrderPrice){

            // Update Portfolio
            portfolioService.updatePortfolio(order.getuId(), order.getStock(), order.getQuantity(), order.getPrice(), calculatedOrderPrice);

            // Debit wallet
            walletService.debit(order.getuId(), calculatedOrderPrice);

            // Create transaction
            transactionsService.createTransaction(order.getuId(), null, order.getStock(), TransactionType.BUY, calculatedOrderPrice,
                    order.getQuantity());

        }

        else throw new RuntimeException("Not enough Balance for buying " + order.getStock() + " stocks : quantity - " + order.getQuantity());

        }

        else {
         // Ordertype - SELL

            Portfolio stockHoldings = portfolioService.getPortfolioByUserId(order.getuId());
            int quantityOfStocks = stockHoldings.getQuantity();
            int quantityOfStocksToSell = order.getQuantity();
            int remainingStocksQuantity =  quantityOfStocks - quantityOfStocksToSell;

            if (quantityOfStocksToSell > quantityOfStocks)
                throw new RuntimeException("Not enough stocks to sell !");

            // Update Portfolio
            if (quantityOfStocksToSell < quantityOfStocks){
                // reduce quantity of stocks from holdings
                portfolioService.updatePortfolio(order.getuId(), order.getStock(), remainingStocksQuantity, stockHoldings.getAvgPrice(),
                        stockHoldings.getTotalInvestment() - remainingStocksQuantity*order.getPrice());
                System.out.println("Sold successfully - stocks : " + order.getStock() + " , quantity : " + order.getQuantity());
            }
            else {
                // delete from portfolio
                portfolioService.deletePortfolio(order.getuId(), order.getStock());
                System.out.println("Sold successfully - stocks : " + order.getStock());
            }

            // Credit Wallet
            walletService.credit(order.getuId(), quantityOfStocksToSell*stockHoldings.getAvgPrice());

            // Create Transaction
            transactionsService.createTransaction(order.getuId(), null, order.getStock(), TransactionType.CREDIT,
                    quantityOfStocksToSell*stockHoldings.getAvgPrice(), order.getQuantity());
        }

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
