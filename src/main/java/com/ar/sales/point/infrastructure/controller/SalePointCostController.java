package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointCostUseCase;
import com.ar.sales.point.domain.model.RouteCost;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.controller.dto.SalePointCostRequest;
import com.ar.sales.point.infrastructure.controller.dto.SalePointCostResponse;
import com.ar.sales.point.infrastructure.exception.ConflictException;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/salepointCosts")
@Tag(name = "SalePointCosts", description = "Operations for sale point costs")
public class SalePointCostController {

    private final SalePointCostUseCase useCase;

    public SalePointCostController(SalePointCostUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(summary = "Create a sale point cost")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody SalePointCostRequest req) {
        SalePointCost domain = SalePointCost.builder()
                .salePointOrigin(new SalePoint(req.getOriginId(), null))
                .salePointDestination(new SalePoint(req.getDestinationId(), null))
                .cost(req.getCost())
                .build();
        SalePointCost saved = null;
        try {
            saved = useCase.createSalePointCost(domain);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SalePointCostResponse(saved.getId(), saved.getSalePointOrigin().id(), saved.getSalePointDestination().id(), saved.getCost()));
    }

    @Operation(summary = "Update a sale point cost")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SalePointCostRequest req) {
        SalePointCost domain = new SalePointCost(id, new SalePoint(req.getOriginId(), null), new SalePoint(req.getDestinationId(), null), req.getCost());
        SalePointCost updated = null;
        try {
            updated = useCase.updateSalePointCost(id, domain);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(new SalePointCostResponse(updated.getId(), updated.getSalePointOrigin().id(), updated.getSalePointDestination().id(), updated.getCost()));
    }

    @Operation(summary = "Get a sale point cost by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        SalePointCost spc = null;
        try {
            spc = useCase.getSalePointCostById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SalePointCostResponse(spc.getId(), spc.getSalePointOrigin().id(), spc.getSalePointDestination().id(), spc.getCost()));
    }

    @Operation(summary = "List all sale point costs")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<SalePointCostResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(useCase.getAllSalePointCosts().stream().map(spc -> new SalePointCostResponse(spc.getId(), spc.getSalePointOrigin() != null ? spc.getSalePointOrigin().id() : null, spc.getSalePointDestination() != null ? spc.getSalePointDestination().id() : null, spc.getCost())).collect(Collectors.toList()));
    }

    @Operation(summary = "Delete a sale point cost")
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        try {
            useCase.deleteSalePointCost(id);
        } catch (ResourceNotFoundException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Get less cost route between sale points")
    @GetMapping(value = "/bestroute/{originId}/{destinationId}", produces = "application/json")
    public ResponseEntity<?> getShortestRoute(@PathVariable Long originId, @PathVariable Long destinationId) {
        RouteCost routeCost = null;
        try {
            routeCost = useCase.calculateRouteCost(originId, destinationId);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(routeCost);
    }
}
