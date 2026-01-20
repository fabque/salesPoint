package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.in.AccreditationUseCase;
import com.ar.sales.point.application.port.out.AccreditationRepositoryPort;
import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccreditationService implements AccreditationUseCase {

    private final SalePointService salePointService;
    private final AccreditationRepositoryPort accreditationRepositoryPort;

    public AccreditationService(SalePointService salePointService, AccreditationRepositoryPort accreditationRepositoryPort) {
        this.salePointService = salePointService;
        this.accreditationRepositoryPort = accreditationRepositoryPort;
    }

    @Override
    public Accreditation createAccreditation(Accreditation accreditation) throws ResourceNotFoundException {
        // validar si el punto de venta existe
        if (salePointService.getSalePointById(accreditation.getSalePoint().getId()) == null) {
            throw new ResourceNotFoundException("SalePoint not found");
        }
        Accreditation savedAccreditation = accreditationRepositoryPort.save(accreditation);
        return savedAccreditation;
    }

    @Override
    public List<Accreditation> getAllAccreditations() {
        return accreditationRepositoryPort.findAll();
    }

    @Override
    public Accreditation getAccreditationById(Long id) throws ResourceNotFoundException {
        return accreditationRepositoryPort.findById(id);
    }
}
