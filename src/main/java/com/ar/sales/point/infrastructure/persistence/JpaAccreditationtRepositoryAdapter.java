package com.ar.sales.point.infrastructure.persistence;

import com.ar.sales.point.application.port.out.AccreditationRepositoryPort;
import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import com.ar.sales.point.infrastructure.persistence.postgres.AccreditationEntity;
import com.ar.sales.point.infrastructure.persistence.postgres.repositories.SpringDataAccreditationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaAccreditationtRepositoryAdapter implements AccreditationRepositoryPort {

    public static final String SALE_POINT_NOT_FOUND = "SalePoint not found";
    public static final String SALE_POINT_ALREADY_EXISTS = "SalePoint already exists";
    private  final SpringDataAccreditationRepository springDataAccreditationRepository;

    public JpaAccreditationtRepositoryAdapter(SpringDataAccreditationRepository springDataAccreditationRepository) {
        this.springDataAccreditationRepository = springDataAccreditationRepository;
    }

    @Override
    public Accreditation save(Accreditation accreditation) {
        AccreditationEntity entity = new AccreditationEntity(
                        accreditation.getSalePoint().getId(),
                        accreditation.getSalePoint().getName(),
                        accreditation.getAmount(),
                        accreditation.getAcreditationDate());
        AccreditationEntity saved =springDataAccreditationRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Accreditation findById(Long id) throws ResourceNotFoundException {
        return springDataAccreditationRepository.findById(id).map(this::toDomain).orElseThrow(() -> new ResourceNotFoundException("Accreditation not found"));
    }


    @Override
    public List<Accreditation> findAll() {
        return springDataAccreditationRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    public Accreditation toDomain(AccreditationEntity entity) {
        SalePoint salePoint = new SalePoint(entity.getSalePointId(), entity.getSalePointName());
        return new Accreditation(entity.getId(), salePoint, salePoint.getName(), entity.getAmount(), entity.getAcreditationDate());
    }

}
