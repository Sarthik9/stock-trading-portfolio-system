package com.learningSpringBoot.Stock.Trading.Portfolio.System.repository;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface StockRepository extends JpaRepository<StockEntity, UUID> {
}
