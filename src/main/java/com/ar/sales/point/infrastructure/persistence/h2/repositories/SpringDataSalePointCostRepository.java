package com.ar.sales.point.infrastructure.persistence.h2.repositories;

import com.ar.sales.point.infrastructure.persistence.h2.SalePointCostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataSalePointCostRepository extends JpaRepository<SalePointCostEntity, Long> {
    Optional<SalePointCostEntity> findByOriginIdAndDestinationId(Long originId, Long destinationId);
}
