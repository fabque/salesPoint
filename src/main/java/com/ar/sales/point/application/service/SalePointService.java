package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
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
    @CacheEvict(value = "salePoints", allEntries = true)
    public SalePoint createSalePoint(SalePoint salePoint) {
        return salePointRepositoryPort.save(salePoint);
    }

    @Override
    @Cacheable(value = "salePoints", key = "#id")
    public SalePoint getSalePointById(Long id) {
        return salePointRepositoryPort.findById(id);
    }

    @Override
    @Cacheable("salePoints")
    public List<SalePoint> getAllSalePoints() {
        return salePointRepositoryPort.findAll();
    }

    @Override
    @CacheEvict(value= "salePoints", allEntries = true)
    public void deleteSalePoint(Long id) {
        salePointRepositoryPort.deleteById(id);
    }
}
