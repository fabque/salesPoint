package com.ar.sales.point.infrastructure.persistance.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "sale_point_costs")
public class SalePointCostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_id")
    private Long originId;

    @Column(name = "destination_id")
    private Long destinationId;

    @Column
    private Double cost;

    public SalePointCostEntity() {
    }

    public SalePointCostEntity(Long id, Long originId, Long destinationId, Double cost) {
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
