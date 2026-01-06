package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.port.in.SalePointCostUseCase;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.controller.dto.SalePointCostRequest;
import com.ar.sales.point.infrastructure.controller.dto.SalePointCostResponse;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/salepoint-costs")
@Tag(name = "SalePointCosts", description = "Operations for sale point costs")
public class SalePointCostController {

    private final SalePointCostUseCase useCase;

    public SalePointCostController(SalePointCostUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(summary = "Create a sale point cost")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public SalePointCostResponse create(@RequestBody SalePointCostRequest req) {
        SalePointCost domain = new SalePointCost(req.getId(), new SalePoint(req.getOriginId(), null), new SalePoint(req.getDestinationId(), null), req.getCost());
        SalePointCost saved = useCase.createSalePointCost(domain);
        return new SalePointCostResponse(saved.getId(), saved.getSalePointOrigin() != null ? saved.getSalePointOrigin().id() : null, saved.getSalePointDestination() != null ? saved.getSalePointDestination().id() : null, saved.getCost());
    }

    @Operation(summary = "Update a sale point cost")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public SalePointCostResponse update(@PathVariable Long id, @RequestBody SalePointCostRequest req) {
        SalePointCost domain = new SalePointCost(id, new SalePoint(req.getOriginId(), null), new SalePoint(req.getDestinationId(), null), req.getCost());
        SalePointCost updated = useCase.updateSalePointCost(id, domain);
        return new SalePointCostResponse(updated.getId(), updated.getSalePointOrigin() != null ? updated.getSalePointOrigin().id() : null, updated.getSalePointDestination() != null ? updated.getSalePointDestination().id() : null, updated.getCost());
    }

    @Operation(summary = "Get a sale point cost by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public SalePointCostResponse getById(@PathVariable Long id) {
        SalePointCost spc = useCase.getSalePointCostById(id);
        if (spc == null) throw new ResourceNotFoundException("SalePointCost with id " + id + " not found");
        return new SalePointCostResponse(spc.getId(), spc.getSalePointOrigin() != null ? spc.getSalePointOrigin().id() : null, spc.getSalePointDestination() != null ? spc.getSalePointDestination().id() : null, spc.getCost());
    }

    @Operation(summary = "List all sale point costs")
    @GetMapping(produces = "application/json")
    public List<SalePointCostResponse> getAll() {
        return useCase.getAllSalePointCosts().stream().map(spc -> new SalePointCostResponse(spc.getId(), spc.getSalePointOrigin() != null ? spc.getSalePointOrigin().id() : null, spc.getSalePointDestination() != null ? spc.getSalePointDestination().id() : null, spc.getCost())).collect(Collectors.toList());
    }

    @Operation(summary = "Delete a sale point cost")
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        useCase.deleteSalePointCost(id);
    }
}
