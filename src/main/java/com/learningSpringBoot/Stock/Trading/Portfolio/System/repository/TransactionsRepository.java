package com.learningSpringBoot.Stock.Trading.Portfolio.System.repository;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.TransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionsRepository extends JpaRepository<TransactionsEntity, UUID> {

     List<TransactionsEntity> findAllByUserId(UUID userId);

}
