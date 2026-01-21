package com.ar.sales.point.infrastructure.persistence.postgres.repositories;

import com.ar.sales.point.infrastructure.persistence.postgres.AccreditationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataAccreditationRepository extends JpaRepository<AccreditationEntity, Long> {
    List<AccreditationEntity> findBySalePointId(Long salePointId);
}
