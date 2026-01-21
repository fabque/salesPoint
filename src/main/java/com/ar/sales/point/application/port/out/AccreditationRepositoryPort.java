package com.ar.sales.point.application.port.out;

import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;

import java.util.List;


public interface AccreditationRepositoryPort {
    Accreditation save(Accreditation accreditation);
    Accreditation findById(Long id) throws ResourceNotFoundException;
    List<Accreditation> findAll();
    List<Accreditation> findBySalePointId(Long salePointId);
}
