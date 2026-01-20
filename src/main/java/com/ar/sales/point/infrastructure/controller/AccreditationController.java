package com.ar.sales.point.infrastructure.controller;

import com.ar.sales.point.application.service.AccreditationService;
import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.infrastructure.controller.dto.AccreditationRequest;
import com.ar.sales.point.infrastructure.controller.dto.AccreditationResponse;
import com.ar.sales.point.infrastructure.controller.dto.SalePointResponse;
import com.ar.sales.point.infrastructure.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/accreditations")
@Tag(name = "Accreditations", description = "APIs for managing accreditations")
public class AccreditationController {

    private final AccreditationService accreditationService;

    public AccreditationController(AccreditationService accreditationService) {
        this.accreditationService = accreditationService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sale point created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<?> createAccreditation(@RequestBody AccreditationRequest request) {
        Accreditation accreditation = new Accreditation(new SalePoint(request.salePointId(), request.salePointName()), request.amount(), request.acreditationDate());
        try {
            Accreditation accreditationSaved = accreditationService.createAccreditation(accreditation);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

        return ResponseEntity.status(201).body(new AccreditationResponse(accreditation.getId(), accreditation.getSalePoint().getId(), accreditation.getSalePoint().getName(), accreditation.getAmount(), accreditation.getAcreditationDate()));
    }

    @Operation(summary = "List all accreditations", description = "Returns all accreditations.")
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllSalePoints() {
        return ResponseEntity.status(HttpStatus.OK).body(accreditationService.getAllAccreditations().stream()
                .map(sp -> new AccreditationResponse(sp.getId(), sp.getSalePoint().getId(), sp.getSalePointName(), sp.getAmount(), sp.getAcreditationDate()))
                .collect(Collectors.toList()));
    }

}
