package com.ar.sales.point.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSalePointRepository extends JpaRepository<SalePointEntity, Long> {
}
