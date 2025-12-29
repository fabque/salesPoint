package com.ar.sales.point.application.port.out;

import com.ar.sales.point.domain.model.SalePoint;

import java.util.List;

public interface SalePointRepositoryPort {

    SalePoint save(SalePoint salePoint);
    SalePoint findById(Long id);
    List<SalePoint> findAll();
    void deleteById(Long id);
}
