package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
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
        when(salePointUseCase.getSalePointById(1L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/salepoints/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("SalePoint with id 1 not found"));
    }
}

