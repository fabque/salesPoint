package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointUseCase;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.controller.dto.SalePointRequest;
import com.ar.sales.point.infrastructure.controller.dto.SalePointResponse;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.http.ResponseEntity;

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
        @ApiResponse(responseCode = "201", description = "Sale point created"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "409", description = "Conflict - Sale point already exists")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> saveSalePoint(@RequestBody SalePointRequest salePointRequest) {
        final SalePoint salePoint = new SalePoint(salePointRequest.getId(), salePointRequest.getName());
        final SalePoint salePointCreated;
        try {
            salePointCreated = salePointUseCase.createSalePoint(salePoint);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SalePointResponse(salePointCreated.id(), salePointCreated.name()));
    }

    @Operation(summary = "Get a sale point by id", description = "Returns a sale point by its id.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getSalePointById(@PathVariable("id") Long id) {
        final SalePoint salePoint;
        try {
            salePoint = salePointUseCase.getSalePointById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SalePointResponse(salePoint.id(), salePoint.name()));
    }

    @Operation(summary = "List all sale points", description = "Returns all sale points.")
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllSalePoints() {
        return ResponseEntity.status(HttpStatus.OK).body(salePointUseCase.getAllSalePoints().stream()
                .map(sp -> new SalePointResponse(sp.id(), sp.name()))
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Delete a sale point by id", description = "Deletes a sale point by its id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not Found - Sale point does not exist")
    })
    @DeleteMapping(value = "/{id}")
    public void deleteSalePoint(@PathVariable("id") Long id) {
        try {
            salePointUseCase.deleteSalePoint(id);
        } catch (ResourceNotFoundException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
