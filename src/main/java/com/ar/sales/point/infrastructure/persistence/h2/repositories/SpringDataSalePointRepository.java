package com.ar.sales.point.infrastructure.persistence.h2.repositories;

import com.ar.sales.point.infrastructure.persistence.h2.SalePointEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpringDataSalePointRepository extends JpaRepository<SalePointEntity, Long> {
}
