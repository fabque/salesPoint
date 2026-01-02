package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.in.SalePointCostUseCase;
import com.ar.sales.point.application.port.out.SalePointCostRepositoryPort;
import com.ar.sales.point.domain.model.SalePointCost;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalePointCostService implements SalePointCostUseCase {

    private final SalePointCostRepositoryPort repository;

    public SalePointCostService(SalePointCostRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @CacheEvict(value = "salePointCosts", allEntries = true)
    public SalePointCost createSalePointCost(SalePointCost salePointCost) {
        return repository.save(salePointCost);
    }

    @Override
    @CachePut(value = "salePointCosts", key = "#id")
    public SalePointCost updateSalePointCost(Long id, SalePointCost salePointCost) {
        // Ensure ID consistency
        salePointCost.setId(id);
        return repository.save(salePointCost);
    }

    @Override
    @Cacheable(value = "salePointCosts", key = "#id")
    public SalePointCost getSalePointCostById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Cacheable("salePointCosts")
    public List<SalePointCost> getAllSalePointCosts() {
        return repository.findAll();
    }

    @CacheEvict(value = "salePointCosts", allEntries = true)
    @Override
    public void deleteSalePointCost(Long id) {
        repository.deleteById(id);
    }
}

