package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalePointService implements SalePointUseCase {

    private final SalePointRepositoryPort salePointRepositoryPort;

    public SalePointService(SalePointRepositoryPort salePointRepositoryPort) {
        this.salePointRepositoryPort = salePointRepositoryPort;
    }

    @Override
    @CacheEvict(value = {"salePoints", "salePointCosts", "routeCosts" }, allEntries = true)
    public SalePoint createSalePoint(SalePoint salePoint) throws ConflictException {
        try {
            return salePointRepositoryPort.save(salePoint);
        } catch (ConflictException e) {
            throw e;
        }
    }

    @Override
    public void saveAllSalePoints(List<SalePoint> salePoints) {
        salePointRepositoryPort.saveAll(salePoints);
    }

    @CachePut(value = "salePoints", key = "#id")
    @CacheEvict(value = {"salePointCosts", "routeCosts" }, allEntries = true)
    public SalePoint updateSalePoint(SalePoint salePoint) throws ResourceNotFoundException {
        try {
            return salePointRepositoryPort.update(salePoint);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @Override
    @Cacheable(value = "salePoints", key = "#id")
    public SalePoint getSalePointById(Long id) throws ResourceNotFoundException {
        try {
            salePointRepositoryPort.findById(id);
        } catch (Exception e) {
            throw e;
        }
        return salePointRepositoryPort.findById(id);
    }

    @Override
    @Cacheable("salePoints")
    public List<SalePoint> getAllSalePoints() {
        return salePointRepositoryPort.findAll();
    }

    @Override
    @CacheEvict(value = {"salePoints", "salePointCosts", "routeCosts" }, allEntries = true)
    public void deleteSalePoint(Long id) throws ResourceNotFoundException {
        try {
            SalePoint deleteEntity = salePointRepositoryPort.findById(id);
            salePointRepositoryPort.deleteById(deleteEntity.getId());
        } catch (Exception e) {
            throw e;
        }
    }
}
