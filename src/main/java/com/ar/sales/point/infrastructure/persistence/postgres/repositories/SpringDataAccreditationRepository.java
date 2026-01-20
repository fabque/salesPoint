package com.ar.sales.point.infrastructure.persistence.postgres.repositories;

import com.ar.sales.point.infrastructure.persistence.postgres.AccreditationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataAccreditationRepository extends JpaRepository<AccreditationEntity, Long> {
}
