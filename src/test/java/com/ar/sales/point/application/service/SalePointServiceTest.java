package com.ar.sales.point.application.service;

import com.ar.sales.point.application.port.out.SalePointRepositoryPort;
import com.ar.sales.point.domain.model.SalePoint;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SalePointServiceTest {

    @Test
    void create_and_get_and_delete_flow() {
        SalePointRepositoryPort repo = mock(SalePointRepositoryPort.class);
        SalePointService service = new SalePointService(repo);

        SalePoint sp = new SalePoint(1L, "Test SP");
        SalePoint saved = new SalePoint(1L, "Test SP");

        when(repo.save(any(SalePoint.class))).thenReturn(saved);
        when(repo.findById(1L)).thenReturn(saved);

        SalePoint created = service.createSalePoint(sp);
        assertNotNull(created);
        assertEquals(1L, created.id());

        SalePoint fetched = service.getSalePointById(1L);
        assertNotNull(fetched);
        assertEquals("Test SP", fetched.name());

        service.deleteSalePoint(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void list_all() {
        SalePointRepositoryPort repo = mock(SalePointRepositoryPort.class);
        SalePointService service = new SalePointService(repo);

        when(repo.findAll()).thenReturn(List.of(new SalePoint(1L, "A"), new SalePoint(2L, "B")));

        List<SalePoint> list = service.getAllSalePoints();
        assertEquals(2, list.size());
    }
}

