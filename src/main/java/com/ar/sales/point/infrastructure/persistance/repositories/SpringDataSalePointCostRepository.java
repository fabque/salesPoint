package com.ar.sales.point.infrastructure.persistance.repositories;

import com.ar.sales.point.infrastructure.persistance.entities.SalePointCostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSalePointCostRepository extends JpaRepository<SalePointCostEntity, Long> {
}

