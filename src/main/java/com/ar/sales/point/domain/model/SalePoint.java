package com.ar.sales.point.domain.model;

public class SalePoint {
    private Long id;
    private String name;

    // No-arg constructor
    public SalePoint() {
    }

    // All-args constructor
    public SalePoint(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
