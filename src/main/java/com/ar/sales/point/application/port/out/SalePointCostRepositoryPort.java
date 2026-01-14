package com.ar.sales.point.application.port.out;

import com.ar.sales.point.domain.model.SalePointCost;

import java.util.List;

public interface SalePointCostRepositoryPort {

    SalePointCost save(SalePointCost salePointCost);
    SalePointCost findById(Long id);
    SalePointCost findByOriginAndDestination(Long originId, Long destinationId);
    List<SalePointCost> findAll();
    void deleteById(Long id);
    void saveAll(List<SalePointCost> salePointCostList);
}

