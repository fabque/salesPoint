package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalePointService implements SalePointUseCase {

    private final SalePointRepositoryPort salePointRepositoryPort;

    public SalePointService(SalePointRepositoryPort salePointRepositoryPort) {
        this.salePointRepositoryPort = salePointRepositoryPort;
    }

    @Override
    public SalePoint createSalePoint(SalePoint salePoint) {
        return salePointRepositoryPort.save(salePoint);
    }

    @Override
    public SalePoint getSalePointById(Long id) {
        return salePointRepositoryPort.findById(id);
    }

    @Override
    public List<SalePoint> getAllSalePoints() {
        return salePointRepositoryPort.findAll();
    }

    @Override
    public void deleteSalePoint(Long id) {
        salePointRepositoryPort.deleteById(id);
    }
}
