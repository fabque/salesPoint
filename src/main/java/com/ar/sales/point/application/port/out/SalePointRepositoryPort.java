package com.ar.sales.point.application.port.out;

import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;

import java.util.List;

public interface SalePointRepositoryPort {

    SalePoint save(SalePoint salePoint) throws ConflictException;
    void saveAll(List<SalePoint> salePoints);
    SalePoint update(SalePoint salePoint) throws ResourceNotFoundException;
    SalePoint findById(Long id) throws ResourceNotFoundException;
    List<SalePoint> findAll();
    void deleteById(Long id) throws ResourceNotFoundException;
}
