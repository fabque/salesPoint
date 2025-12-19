package com.ar.sales.point.application.port.out;

import com.ar.sales.point.domain.model.SalePoint;

public interface SalePointRepositoryPort {

    SalePoint save(SalePoint salePoint);
}
