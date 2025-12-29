package com.ar.sales.point.application.port.in;

import com.ar.sales.point.domain.model.SalePointCost;

import java.util.List;

public interface SalePointCostUseCase {

    SalePointCost createSalePointCost(SalePointCost salePointCost);
    SalePointCost updateSalePointCost(Long id, SalePointCost salePointCost);
    SalePointCost getSalePointCostById(Long id);
    List<SalePointCost> getAllSalePointCosts();
    void deleteSalePointCost(Long id);
}
