package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointCostUseCase;
import com.ar.sales.point.infrastructure.controller.dto.SalePointCostRequest;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalePointCostController.class)
public class SalePointCostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalePointCostUseCase useCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_conflict_returns409_handledByAdvice() throws Exception {
        SalePointCostRequest request = new SalePointCostRequest(1L, 2L, 100.0);

        when(useCase.createSalePointCost(any()))
                .thenThrow(new ConflictException("Already exists"));

        mockMvc.perform(post("/salepointCosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Already exists")));
    }

    @Test
    void update_notFound_returns404_handledByAdvice() throws Exception {
        SalePointCostRequest request = new SalePointCostRequest(1L, 2L, 150.0);

        when(useCase.updateSalePointCost(eq(10L), any()))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(put("/salepointCosts/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Not found")));
    }

    @Test
    void getById_notFound_returns404_handledByAdvice() throws Exception {
        when(useCase.getSalePointCostById(99L))
                .thenThrow(new ResourceNotFoundException("SalePointCost not found"));

        mockMvc.perform(get("/salepointCosts/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("SalePointCost not found")));
    }

    @Test
    void delete_notFound_returns404_handledByAdvice() throws Exception {
        doThrow(new ResourceNotFoundException("Not found"))
                .when(useCase).deleteSalePointCost(any());

        mockMvc.perform(delete("/salepointCosts/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Not found")));
    }

    @Test
    void bestRoute_notFound_returns404_handledByAdvice() throws Exception {
        when(useCase.calculateRouteCost(1L, 5L))
                .thenThrow(new ResourceNotFoundException("Route not found"));

        mockMvc.perform(get("/salepointCosts/bestroute/1/5"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Route not found")));
    }


}
