package com.ar.sales.point.infrastructure.controller.dto;

public class SalePointCostRequest {
    private Long originId;
    private Long destinationId;
    private Double cost;

    public SalePointCostRequest() {
    }

    public SalePointCostRequest(Long originId, Long destinationId, Double cost) {
        this.originId = originId;
        this.destinationId = destinationId;
        this.cost = cost;
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

