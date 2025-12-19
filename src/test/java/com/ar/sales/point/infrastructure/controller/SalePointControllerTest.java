package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.controller.dto.SalePointRequest;
import com.ar.sales.point.infrastructure.controller.dto.SalePointResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SalePointControllerTest {

    @Test
    void saveSalePoint_returnsResponse() {
        // Arrange
        SalePointUseCase useCase = mock(SalePointUseCase.class);
        SalePointController controller = new SalePointController(useCase);

        SalePointRequest req = new SalePointRequest();
        req.setId(1L);
        req.setName("My SP");

        SalePoint returned = new SalePoint(1L, "My SP");
        when(useCase.createSalePoint(any(SalePoint.class))).thenReturn(returned);

        // Act
        SalePointResponse resp = controller.saveSalePoint(req);

        // Assert
        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("My SP", resp.getName());
        verify(useCase, times(1)).createSalePoint(any(SalePoint.class));
    }
}

