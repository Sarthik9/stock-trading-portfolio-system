package com.learningSpringBoot.Stock.Trading.Portfolio.System.Repository;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.Entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface StockRepository extends JpaRepository<StockEntity, UUID> {

//    public StockEntity getOrderByUserId(StockRequest requestObj) {
//        return executeQuery(requestObj);
//    }
//
//    private StockEntity executeQuery(StockRequest requestObj) {
//        StockEntity entity = new StockEntity();
//        entity.setuId(requestObj.getUserId());
//        entity.setStock("Tesla");
//        entity.setPrice(250);
//        entity.setQuantity(10);
//        entity.setOrderType("Buy");
//        return entity;
//    }
//
//    public StockEntity createOrder(Order order){
//        return executeOrder(order);
//    }
//
//    private StockEntity executeOrder(Order order) {
//        StockEntity entity = new StockEntity();
//        entity.setuId(order.getuId());
//        entity.setStock(order.getStock());
//        entity.setPrice(order.getPrice());
//        entity.setOrderType(order.getOrderType());
//        entity.setQuantity(order.getQuantity());
//        return entity;
//    }
}
