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

    private final SalePointService salePointService;



    public SalePointCostService(SalePointCostRepositoryPort repository, SalePointService salePointService) {
        this.repository = repository;
        this.salePointService = salePointService;
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
            SalePoint origin = salePointService.getSalePointById(salePointCost.getSalePointOrigin().id());
            SalePoint destination = salePointService.getSalePointById(salePointCost.getSalePointDestination().id());
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
    @CacheEvict(value = "routeCosts", allEntries = true)
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

    @CacheEvict(value = {"salePointCosts", "routeCosts"}, allEntries = true)
    @Override
    public void deleteSalePointCost(Long id) throws ResourceNotFoundException {
        SalePointCost found = repository.findById(id);
        if (found == null) {
            throw new ResourceNotFoundException(SALE_POINT_COST_NOT_FOUND);
        }
        repository.deleteById(id);
    }

    @Override
    @Cacheable(value = "routeCosts", key = "#originId + '-' + #destinationId")
    public RouteCost calculateRouteCost(Long originId, Long destinationId) throws ResourceNotFoundException {

        try {
            SalePoint OriginSalePoint = salePointService.getSalePointById(originId);
            SalePoint DestinationSalePoint = salePointService.getSalePointById(destinationId);
            List<SalePoint> allSalePoints = salePointService.getAllSalePoints();
            List<SalePointCost> allSalePointCosts = repository.findAll();
            RouteCost routeCost = CalculateRouteUtil.getOptimalRoute(OriginSalePoint, DestinationSalePoint, allSalePoints, allSalePointCosts);
            if (routeCost == null) {
                throw new ResourceNotFoundException("No route found between the specified sale points");
            }
            return routeCost;
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @Override
    public void initCostDDBB() {
        SalePoint caba = new SalePoint(1L, "CABA");
        SalePoint gba1 = new SalePoint(2L, "Gran Buenos Aires 1");
        SalePoint gba2 = new SalePoint(3L, "Gran Buenos Aires 2");
        SalePoint santaFe = new SalePoint(4L, "Santa Fe");
        SalePoint cordoba = new SalePoint(5L, "Córdoba");
        SalePoint misiones = new SalePoint(6L, "Misiones");
        SalePoint salta = new SalePoint(7L, "Salta");
        SalePoint chubut = new SalePoint(8L, "Chubut");
        SalePoint santaCruz = new SalePoint(9L, "Santa Cruz");
        SalePoint catamarca = new SalePoint(10L, "Catamarca");
        salePointService.saveAllSalePoints(List.of (caba, gba1, gba2, santaFe, cordoba, misiones, salta, chubut, santaCruz, catamarca));

        SalePointCost salePointCost1 = SalePointCost.builder().salePointOrigin(caba).salePointDestination(gba1).cost(2.0).build();
        SalePointCost salePointCost2 = SalePointCost.builder().salePointOrigin(caba).salePointDestination(gba2).cost(3.0).build();
        SalePointCost salePointCost3 = SalePointCost.builder().salePointOrigin(gba1).salePointDestination(gba2).cost(5.0).build();
        SalePointCost salePointCost4 = SalePointCost.builder().salePointOrigin(gba1).salePointDestination(santaFe).cost(10.0).build();
        SalePointCost salePointCost5 = SalePointCost.builder().salePointOrigin(caba).salePointDestination(santaFe).cost(11.0).build();
        SalePointCost salePointCost6 = SalePointCost.builder().salePointOrigin(santaFe).salePointDestination(cordoba).cost(5.0).build();
        SalePointCost salePointCost7 = SalePointCost.builder().salePointOrigin(gba1).salePointDestination(cordoba).cost(14.0).build();
        SalePointCost salePointCost8 = SalePointCost.builder().salePointOrigin(misiones).salePointDestination(salta).cost(32.0).build();
        SalePointCost salePointCost9 = SalePointCost.builder().salePointOrigin(chubut).salePointDestination(santaCruz).cost(11.0).build();
        SalePointCost salePointCost10 = SalePointCost.builder().salePointOrigin(catamarca).salePointDestination(salta).cost(5.0).build();
        SalePointCost salePointCost11 = SalePointCost.builder().salePointOrigin(gba2).salePointDestination(chubut).cost(10.0).build();
        SalePointCost salePointCost12 = SalePointCost.builder().salePointOrigin(cordoba).salePointDestination(chubut).cost(30.0).build();
        SalePointCost salePointCost13 = SalePointCost.builder().salePointOrigin(catamarca).salePointDestination(cordoba).cost(5.0).build();
        SalePointCost salePointCost14 = SalePointCost.builder().salePointOrigin(santaFe).salePointDestination(misiones).cost(6.0).build();
        repository.saveAll(List.of(
                salePointCost1,
                salePointCost2,
                salePointCost3,
                salePointCost4,
                salePointCost5,
                salePointCost6,
                salePointCost7,
                salePointCost8,
                salePointCost9,
                salePointCost10,
                salePointCost11,
                salePointCost12,
                salePointCost13,
                salePointCost14
        ));
/*
IdA, IdB, Costo
1,2,2
1,3,3
2,3,5
2,4,10
1,4,11
4,5,5
2,5,14
6,7,32
8,9,11
10,7,5
3,8,10
5,8,30
10,5,5
4,6,6 */
    }
}
