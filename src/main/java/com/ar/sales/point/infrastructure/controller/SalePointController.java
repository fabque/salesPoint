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
}
