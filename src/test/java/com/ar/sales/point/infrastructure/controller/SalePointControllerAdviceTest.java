package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.controller.dto.SalePointRequest;
import com.ar.sales.point.infrastructure.controller.dto.SalePointResponse;
import com.ar.sales.point.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SalePointController.class)
@Import(GlobalExceptionHandler.class)
public class SalePointControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalePointUseCase salePointUseCase;

    @Test
    void getNonExistingSalePoint_returns404WithErrorResponse() throws Exception {
        // Arrange
        when(salePointUseCase.getSalePointById(1L)).thenThrow(new ResourceNotFoundException("SalePoint with id 1 not found"));

        // Act & Assert
        mockMvc.perform(get("/salepoints/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("SalePoint with id 1 not found"));
    }

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
