package com.ar.sales.point.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSalePointCostRepository extends JpaRepository<SalePointCostEntity, Long> {
}

