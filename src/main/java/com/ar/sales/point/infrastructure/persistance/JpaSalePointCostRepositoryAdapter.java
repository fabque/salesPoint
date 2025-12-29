package com.ar.sales.point.infrastructure.persistance;

import com.ar.sales.point.application.port.out.SalePointCostRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaSalePointCostRepositoryAdapter implements SalePointCostRepositoryPort {

    private final SpringDataSalePointCostRepository springDataRepo;

    public JpaSalePointCostRepositoryAdapter(SpringDataSalePointCostRepository springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    @Override
    public SalePointCost save(SalePointCost salePointCost) {
        SalePointCostEntity entity = new SalePointCostEntity(
                salePointCost.getId(),
                salePointCost.getSalePointOrigin() != null ? salePointCost.getSalePointOrigin().getId() : null,
                salePointCost.getSalePointDestination() != null ? salePointCost.getSalePointDestination().getId() : null,
                salePointCost.getCost()
        );
        SalePointCostEntity saved = springDataRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public SalePointCost findById(Long id) {
        return springDataRepo.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<SalePointCost> findAll() {
        return springDataRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        springDataRepo.deleteById(id);
    }

    private SalePointCost toDomain(SalePointCostEntity e) {
        SalePoint origin = e.getOriginId() != null ? new SalePoint(e.getOriginId(), null) : null;
        SalePoint destination = e.getDestinationId() != null ? new SalePoint(e.getDestinationId(), null) : null;
        return new SalePointCost(e.getId(), origin, destination, e.getCost());
    }
}

