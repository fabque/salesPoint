package com.ar.sales.point.application.port.in;

import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;

import java.util.List;

public interface AccreditationUseCase {
    Accreditation createAccreditation(Accreditation accreditation) throws ResourceNotFoundException;
    List<Accreditation> getAllAccreditations();
    Accreditation getAccreditationById(Long id) throws ResourceNotFoundException;
}
