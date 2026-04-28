package com.learningSpringBoot.Stock.Trading.Portfolio.System.repository;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, UUID> {

    List<StockEntity> findAllByUid(UUID uid);

    Optional<StockEntity> findByUidAndStock(UUID uid, String stock);

}
