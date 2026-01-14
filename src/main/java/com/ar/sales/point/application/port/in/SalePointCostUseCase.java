package com.ar.sales.point.application.port.in;

import com.ar.sales.point.domain.model.RouteCost;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;

import java.util.List;

public interface SalePointCostUseCase {

    SalePointCost createSalePointCost(SalePointCost salePointCost) throws ConflictException;
    SalePointCost updateSalePointCost(Long id, SalePointCost salePointCost) throws ResourceNotFoundException;
    SalePointCost getSalePointCostById(Long id) throws ResourceNotFoundException;
    List<SalePointCost> getAllSalePointCosts();
    void deleteSalePointCost(Long id) throws ResourceNotFoundException;
    RouteCost calculateRouteCost(Long originId, Long destinationId) throws ResourceNotFoundException;
    void initCostDDBB();
    void clearAllCaches();
}
