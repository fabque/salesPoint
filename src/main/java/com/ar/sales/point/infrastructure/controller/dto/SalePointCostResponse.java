package com.ar.sales.point.infrastructure.controller.dto;

public class SalePointCostResponse {
    private Long id;
    private Long originId;
    private Long destinationId;
    private Double cost;

    public SalePointCostResponse() {
    }

    public SalePointCostResponse(Long id, Long originId, Long destinationId, Double cost) {
        this.id = id;
        this.originId = originId;
        this.destinationId = destinationId;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}

