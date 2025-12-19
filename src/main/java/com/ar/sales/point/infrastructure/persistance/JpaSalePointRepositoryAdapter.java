package com.ar.sales.point.infrastructure.persistance;

import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import org.springframework.stereotype.Repository;

@Repository
public class JpaSalePointRepositoryAdapter implements SalePointRepositoryPort {

    private  final SpringDataSalePointRepository springDataSalePointRepository;

    public JpaSalePointRepositoryAdapter(SpringDataSalePointRepository springDataSalePointRepository) {
        this.springDataSalePointRepository = springDataSalePointRepository;
    }

    @Override
    public SalePoint save(SalePoint salePoint) {
        SalePointEntity entity = new SalePointEntity(salePoint.getId(), salePoint.getName());
        SalePointEntity saveEntity = springDataSalePointRepository.save(entity);
        return new SalePoint(saveEntity.getId(), saveEntity.getName());
    }
}
