package com.learningSpringBoot.Stock.Trading.Portfolio.System.repository;

import com.learningSpringBoot.Stock.Trading.Portfolio.System.entity.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, UUID> {

    List<PortfolioEntity> findAllByUid(UUID uid);

    Optional<PortfolioEntity> findByUidAndStock(UUID uid, String stock);

}
