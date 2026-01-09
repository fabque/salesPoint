package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.in.SalePointCostUseCase;
import com.ar.sales.point.application.port.out.SalePointCostRepositoryPort;
import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.application.service.utils.CalculateRouteUtil;
import com.ar.sales.point.domain.model.RouteCost;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalePointCostService implements SalePointCostUseCase {

    public static final String SALE_POINT_COST_NOT_FOUND = "SalePointCost not found";
    private final SalePointCostRepositoryPort repository;

    private final SalePointRepositoryPort salePointRepository;

    public SalePointCostService(SalePointCostRepositoryPort repository, SalePointRepositoryPort salePointRepository) {
        this.repository = repository;
        this.salePointRepository = salePointRepository;
    }

    @Override
    @CacheEvict(value = "salePointCosts", allEntries = true)
    public SalePointCost createSalePointCost(SalePointCost salePointCost) throws ConflictException {
        //validations
        if (!isValidSalePointCost(salePointCost)) {
            throw new ConflictException("Invalid SalePointCost data");
        }
        return repository.save(salePointCost);
    }

    private boolean isValidSalePointCost(SalePointCost salePointCost) {
         try {
            SalePoint origin = salePointRepository.findById(salePointCost.getSalePointOrigin().id());
            SalePoint destination = salePointRepository.findById(salePointCost.getSalePointDestination().id());
        } catch (ResourceNotFoundException e) {
            return false;
        }
        if (salePointCost.getCost() < 0) {
            return false;
        }
        if (salePointCost.getSalePointOrigin().id().equals(salePointCost.getSalePointDestination().id())
        && salePointCost.getCost() != 0) {
            return false;
        }
        //  validar camino directo A a B o B a A
        SalePointCost existsDirectRoute = repository.findByOriginAndDestination(
                salePointCost.getSalePointOrigin().id(),
                salePointCost.getSalePointDestination().id()
        );
        if (existsDirectRoute != null) {
            return false;
        }
        return true;
    }

    @Override
    @CachePut(value = "salePointCosts", key = "#id")
    public SalePointCost updateSalePointCost(Long id, SalePointCost salePointCost) throws ResourceNotFoundException{
        // Ensure resource exists
        SalePointCost existing = repository.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException(SALE_POINT_COST_NOT_FOUND);
        }
        salePointCost.setCost(salePointCost.getCost());
        return repository.save(salePointCost);
    }

    @Override
    @Cacheable(value = "salePointCosts", key = "#id")
    public SalePointCost getSalePointCostById(Long id) throws ResourceNotFoundException {
        SalePointCost found = repository.findById(id);
        if (found == null) {
            throw new ResourceNotFoundException(SALE_POINT_COST_NOT_FOUND);
        }
        return found;
    }

    @Override
    @Cacheable("salePointCosts")
    public List<SalePointCost> getAllSalePointCosts() {
        return repository.findAll();
    }

    @CacheEvict(value = "salePointCosts", allEntries = true)
    @Override
    public void deleteSalePointCost(Long id) throws ResourceNotFoundException {
        SalePointCost found = repository.findById(id);
        if (found == null) {
            throw new ResourceNotFoundException(SALE_POINT_COST_NOT_FOUND);
        }
        repository.deleteById(id);
    }

    @Override
    public RouteCost calculateRouteCost(Long originId, Long destinationId) throws ResourceNotFoundException {

        try {
            SalePoint OriginSalePoint = salePointRepository.findById(originId);
            SalePoint DestinationSalePoint = salePointRepository.findById(destinationId);
            List<SalePoint> allSalePoints = salePointRepository.findAll();
            List<SalePointCost> allSalePointCosts = repository.findAll();
            RouteCost routeCost = CalculateRouteUtil.getOptimalRoute(OriginSalePoint, DestinationSalePoint, allSalePoints, allSalePointCosts);
            return routeCost;
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }
}
