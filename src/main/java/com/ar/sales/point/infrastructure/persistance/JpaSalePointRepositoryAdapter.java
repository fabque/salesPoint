package com.ar.sales.point.infrastructure.persistance;

import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaSalePointRepositoryAdapter implements SalePointRepositoryPort {

    private  final SpringDataSalePointRepository springDataSalePointRepository;

    public JpaSalePointRepositoryAdapter(SpringDataSalePointRepository springDataSalePointRepository) {
        this.springDataSalePointRepository = springDataSalePointRepository;
    }

    @Override
    public SalePoint save(SalePoint salePoint) {
        SalePointEntity entity = new SalePointEntity(salePoint.id(), salePoint.name());
        SalePointEntity saveEntity = springDataSalePointRepository.save(entity);
        return new SalePoint(saveEntity.getId(), saveEntity.getName());
    }

    @Override
    public SalePoint findById(Long id) {
        return springDataSalePointRepository.findById(id)
                .map(e -> new SalePoint(e.getId(), e.getName()))
                .orElse(null);
    }

    @Override
    public List<SalePoint> findAll() {
        return springDataSalePointRepository.findAll().stream()
                .map(e -> new SalePoint(e.getId(), e.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        springDataSalePointRepository.deleteById(id);
    }
}
