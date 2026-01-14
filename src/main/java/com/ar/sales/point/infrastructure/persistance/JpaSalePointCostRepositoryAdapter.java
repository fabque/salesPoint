package com.ar.sales.point.infrastructure.persistance;

import com.ar.sales.point.application.port.out.SalePointCostRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.persistance.entities.SalePointCostEntity;
import com.ar.sales.point.infrastructure.persistance.repositories.SpringDataSalePointCostRepository;
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
                salePointCost.getId() != null ? salePointCost.getId() : null,
                salePointCost.getSalePointOrigin() != null ? salePointCost.getSalePointOrigin().id() : null,
                salePointCost.getSalePointDestination() != null ? salePointCost.getSalePointDestination().id() : null,
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
    public SalePointCost findByOriginAndDestination(Long originId, Long destinationId) {
         SalePointCost salePointCost = springDataRepo.findByOriginIdAndDestinationId(originId, destinationId)
                .map(this::toDomain)
                .orElse(null);
         if (salePointCost == null) {
             salePointCost = springDataRepo.findByOriginIdAndDestinationId(destinationId, originId)
                    .map(this::toDomain)
                    .orElse(null);
         }
         return salePointCost;
    }

    @Override
    public List<SalePointCost> findAll() {
        return springDataRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        springDataRepo.deleteById(id);
    }

    @Override
    public void saveAll(List<SalePointCost> salePointCostList) {
        springDataRepo.saveAll(salePointCostList.stream().map(sc->
                new SalePointCostEntity(
                        sc.getId() != null ? sc.getId() : null,
                        sc.getSalePointOrigin().id(),
                        sc.getSalePointDestination().id(),
                        sc.getCost()
                )
        ).collect(Collectors.toList()));
    }

    private SalePointCost toDomain(SalePointCostEntity e) {
        SalePoint origin = e.getOriginId() != null ? new SalePoint(e.getOriginId(), null) : null;
        SalePoint destination = e.getDestinationId() != null ? new SalePoint(e.getDestinationId(), null) : null;
        return new SalePointCost(e.getId(), origin, destination, e.getCost());
    }
}
