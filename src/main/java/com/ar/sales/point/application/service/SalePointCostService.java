package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.in.SalePointCostUseCase;
import com.ar.sales.point.application.port.out.SalePointCostRepositoryPort;
import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalePointCostService implements SalePointCostUseCase {

    public static final String SALE_POINT_COST_NOT_FOUND = "SalePointCost not found";
    private final SalePointCostRepositoryPort repository;

    private final SalePointRepositoryPort salePointRepository;

    public SalePointCostService(SalePointCostRepositoryPort repository, SalePointRepositoryPort salePointRepository) {
        this.repository = repository;
        this.salePointRepository = salePointRepository;
    }

    @Override
    @CacheEvict(value = "salePointCosts", allEntries = true)
    public SalePointCost createSalePointCost(SalePointCost salePointCost) {
        //validations
        if (!isValidSalePointCost(salePointCost)) {
            throw new IllegalArgumentException("Invalid SalePointCost data");
        }
        return repository.save(salePointCost);
    }

    private boolean isValidSalePointCost(SalePointCost salePointCost) {
        // validar existencia de Salepoints
        try {
            SalePoint origin = salePointRepository.findById(salePointCost.getSalePointOrigin().id());
            SalePoint destination = salePointRepository.findById(salePointCost.getSalePointDestination().id());
        } catch (ResourceNotFoundException e) {
            return false;
        }
        // validar que los costos sean positivos en caso de A a A tiene que ser 0
        if (salePointCost.getCost() < 0) {
            return false;
        }
        if (salePointCost.getSalePointOrigin().id().equals(salePointCost.getSalePointDestination().id())
        && salePointCost.getCost() != 0) {
            return false;
        }
        // todo validar camino directo A a B o B a A

        return true;
    }

    @Override
    @CachePut(value = "salePointCosts", key = "#id")
    public SalePointCost updateSalePointCost(Long id, SalePointCost salePointCost) {
        // Ensure resource exists
        SalePointCost existing = repository.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException(SALE_POINT_COST_NOT_FOUND);
        }
        salePointCost.setCost(salePointCost.getCost());
        return repository.save(salePointCost);
    }

    @Override
    @Cacheable(value = "salePointCosts", key = "#id")
    public SalePointCost getSalePointCostById(Long id) {
        SalePointCost found = repository.findById(id);
        if (found == null) {
            throw new ResourceNotFoundException(SALE_POINT_COST_NOT_FOUND);
        }
        return found;
    }

    @Override
    @Cacheable("salePointCosts")
    public List<SalePointCost> getAllSalePointCosts() {
        return repository.findAll();
    }

    @CacheEvict(value = "salePointCosts", allEntries = true)
    @Override
    public void deleteSalePointCost(Long id) {
        SalePointCost found = repository.findById(id);
        if (found == null) {
            throw new ResourceNotFoundException(SALE_POINT_COST_NOT_FOUND);
        }repository.deleteById(id);
    }
}
