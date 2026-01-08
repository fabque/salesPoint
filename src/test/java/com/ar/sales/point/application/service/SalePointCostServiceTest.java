package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.out.SalePointCostRepositoryPort;
import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SalePointCostServiceTest {

    @Test
    void create_update_get_delete_flow() {
        SalePointCostRepositoryPort repo = mock(SalePointCostRepositoryPort.class);
        SalePointRepositoryPort salePointRepo = mock(SalePointRepositoryPort.class);
        SalePointCostService service = new SalePointCostService(repo, salePointRepo);

        SalePointCost input = new SalePointCost(null, new SalePoint(1L, "A"), new SalePoint(2L, "B"), 10.0);
        SalePointCost saved = new SalePointCost(1L, new SalePoint(1L, "A"), new SalePoint(2L, "B"), 10.0);

        when(repo.save(any(SalePointCost.class))).thenReturn(saved);
        when(repo.findById(1L)).thenReturn(saved);

        SalePointCost created = service.createSalePointCost(input);
        assertNotNull(created);
        assertEquals(1L, created.getId());

        SalePointCost fetched = service.getSalePointCostById(1L);
        assertNotNull(fetched);
        assertEquals(10.0, fetched.getCost());

        SalePointCost updatedInput = new SalePointCost(1L, new SalePoint(1L, "A"), new SalePoint(2L, "B"), 12.5);
        SalePointCost updatedSaved = new SalePointCost(1L, new SalePoint(1L, "A"), new SalePoint(2L, "B"), 12.5);
        when(repo.save(any(SalePointCost.class))).thenReturn(updatedSaved);

        SalePointCost updated = service.updateSalePointCost(1L, updatedInput);
        assertNotNull(updated);
        assertEquals(12.5, updated.getCost());

        service.deleteSalePointCost(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void list_all() {
        SalePointCostRepositoryPort repo = mock(SalePointCostRepositoryPort.class);
        SalePointRepositoryPort salePointRepo = mock(SalePointRepositoryPort.class);
        SalePointCostService service = new SalePointCostService(repo, salePointRepo);

        when(repo.findAll()).thenReturn(List.of(
                new SalePointCost(1L, new SalePoint(1L, "A"), new SalePoint(2L, "B"), 5.0),
                new SalePointCost(2L, new SalePoint(1L, "A"), new SalePoint(3L, "C"), 7.5)
        ));

        List<SalePointCost> list = service.getAllSalePointCosts();
        assertEquals(2, list.size());
    }
}

