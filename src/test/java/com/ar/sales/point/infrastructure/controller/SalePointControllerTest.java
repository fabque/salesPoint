package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.controller.dto.SalePointRequest;
import com.ar.sales.point.infrastructure.controller.dto.SalePointResponse;
import com.ar.sales.point.infrastructure.exception.GlobalExceptionHandler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SalePointController.class)
@Import(GlobalExceptionHandler.class)
public class SalePointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalePointUseCase salePointUseCase;

    @Test
    void getNonExistingSalePoint_returns404WithErrorResponse() throws Exception {
        // Arrange
        when(salePointUseCase.getSalePointById(1L)).thenThrow(new ResourceNotFoundException("SalePoint with id 1 not found"));

        // Act & Assert
        mockMvc.perform(get("/salepoints/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("SalePoint with id 1 not found"));
    }

    @SneakyThrows
    @Test
    void saveSalePoint_returnsResponse() {
        // Arrange
        SalePointUseCase useCase = mock(SalePointUseCase.class);
        SalePointController controller = new SalePointController(useCase);

        SalePointRequest req = new SalePointRequest(1L, "My SP");

        SalePoint returned = new SalePoint(1L, "My SP");
        when(useCase.createSalePoint(any(SalePoint.class))).thenReturn(returned);

        // Act
        ResponseEntity<SalePointResponse> resp = (ResponseEntity<SalePointResponse>) controller.saveSalePoint(req);

        // Assert
        assertNotNull(resp);
        assertEquals(1L, resp.getBody().id());
        assertEquals("My SP", resp.getBody().name());
        verify(useCase, times(1)).createSalePoint(any(SalePoint.class));
    }
}
