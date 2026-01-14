package com.ar.sales.point.application.port.in;

import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;

import java.util.List;

public interface SalePointUseCase {

    SalePoint createSalePoint(SalePoint salePoint) throws ConflictException;
    void saveAllSalePoints(List<SalePoint> salePoints);
    SalePoint updateSalePoint(SalePoint salePoint) throws ResourceNotFoundException;
    SalePoint getSalePointById(Long id) throws ResourceNotFoundException;
    List<SalePoint> getAllSalePoints();
    void deleteSalePoint(Long id) throws ResourceNotFoundException;
}
