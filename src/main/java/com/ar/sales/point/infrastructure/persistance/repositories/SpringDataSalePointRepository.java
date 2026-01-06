package com.ar.sales.point.infrastructure.persistance.repositories;

import com.ar.sales.point.infrastructure.persistance.entities.SalePointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSalePointRepository extends JpaRepository<SalePointEntity, Long> {
}
