package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.controller.dto.SalePointRequest;
import com.ar.sales.point.infrastructure.controller.dto.SalePointResponse;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/salepoints")
@Tag(name = "SalePoints", description = "Operations for sale points")
public class SalePointController {

    private final SalePointUseCase salePointUseCase;

    public SalePointController(SalePointUseCase salePointUseCase) {
        this.salePointUseCase = salePointUseCase;
    }

    @Operation(summary = "Create a sale point", description = "Creates a new sale point and returns the created resource.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sale point created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public SalePointResponse saveSalePoint(@RequestBody SalePointRequest salePointRequest) {
        final SalePoint salePoint = new SalePoint(salePointRequest.getId(), salePointRequest.getName());
        final SalePoint salePointCreated = salePointUseCase.createSalePoint(salePoint);
        return new SalePointResponse(salePointCreated.getId(), salePointCreated.getName());
    }

    @Operation(summary = "Get a sale point by id", description = "Returns a sale point by its id.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public SalePointResponse getSalePointById(@PathVariable("id") Long id) {
        final SalePoint salePoint = salePointUseCase.getSalePointById(id);
        if (salePoint == null) {
            return null;
        }
        return new SalePointResponse(salePoint.getId(), salePoint.getName());
    }

    @Operation(summary = "List all sale points", description = "Returns all sale points.")
    @GetMapping(produces = "application/json")
    public List<SalePointResponse> getAllSalePoints() {
        return salePointUseCase.getAllSalePoints().stream()
                .map(sp -> new SalePointResponse(sp.getId(), sp.getName()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Delete a sale point by id", description = "Deletes a sale point by its id.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Deleted")
    @org.springframework.web.bind.annotation.DeleteMapping(value = "/{id}")
    public void deleteSalePoint(@PathVariable("id") Long id) {
        salePointUseCase.deleteSalePoint(id);
    }
}
