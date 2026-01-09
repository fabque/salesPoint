package com.ar.sales.point.infrastructure.persistance.repositories;

import com.ar.sales.point.infrastructure.persistance.entities.SalePointCostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataSalePointCostRepository extends JpaRepository<SalePointCostEntity, Long> {
    Optional<SalePointCostEntity> findByOriginIdAndDestinationId(Long originId, Long destinationId);
}
