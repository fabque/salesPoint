package com.ar.sales.point.infrastructure.persistance;

import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import com.ar.sales.point.infrastructure.persistance.entities.SalePointEntity;
import com.ar.sales.point.infrastructure.persistance.repositories.SpringDataSalePointRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaSalePointRepositoryAdapter implements SalePointRepositoryPort {

    public static final String SALE_POINT_NOT_FOUND = "SalePoint not found";
    public static final String SALE_POINT_ALREADY_EXISTS = "SalePoint already exists";
    private  final SpringDataSalePointRepository springDataSalePointRepository;

    public JpaSalePointRepositoryAdapter(SpringDataSalePointRepository springDataSalePointRepository) {
        this.springDataSalePointRepository = springDataSalePointRepository;
    }

    @Override
    public SalePoint save(SalePoint salePoint) throws ConflictException {
        Optional<SalePointEntity> existing = springDataSalePointRepository.findById(salePoint.id());
        if (existing.isPresent()) {
            throw new ConflictException(SALE_POINT_ALREADY_EXISTS);
        }
        SalePointEntity entityNew = new SalePointEntity(salePoint.id(), salePoint.name());
        SalePointEntity saveEntity = springDataSalePointRepository.save(entityNew);
        return new SalePoint(saveEntity.getId(), saveEntity.getName());
    }

    @Override
    public void saveAll(List<SalePoint> salePoints) {
        springDataSalePointRepository.saveAll(salePoints.stream().map(z ->
                new SalePointEntity(z.id(), z.name())
        ).collect(Collectors.toList()));
    }


    @Override
    public SalePoint update(SalePoint salePoint) throws ResourceNotFoundException {
        SalePointEntity entity = springDataSalePointRepository.findById(salePoint.id()).orElseThrow(() ->
                new ResourceNotFoundException(SALE_POINT_NOT_FOUND));
        // Apply changes from domain object
        entity.setName(salePoint.name());
        SalePointEntity updatedEntity = springDataSalePointRepository.save(entity);
        return new SalePoint(updatedEntity.getId(), updatedEntity.getName());
    }

    @Override
    public SalePoint findById(Long id) throws ResourceNotFoundException {
        return springDataSalePointRepository.findById(id)
                .map(e -> new SalePoint(e.getId(), e.getName()))
                .orElseThrow(() -> new ResourceNotFoundException(SALE_POINT_NOT_FOUND));
    }

    @Override
    public List<SalePoint> findAll() {
        return springDataSalePointRepository.findAll().stream()
                .map(e -> new SalePoint(e.getId(), e.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        springDataSalePointRepository.deleteById(id);
    }
}
