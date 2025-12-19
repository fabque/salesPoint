package com.ar.sales.point.application.port.in;

import com.ar.sales.point.domain.model.SalePoint;

import java.util.List;

public interface SalePointUseCase {

    SalePoint createSalePoint(SalePoint salePoint);
    SalePoint getSalePointById(Long id);
    List<SalePoint> getAllSalePoints();
    void deleteSalePoint(Long id);
}
