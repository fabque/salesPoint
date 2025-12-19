package com.ar.sales.point.infrastructure.controller.dto;

public class SalePointResponse {
    private Long id;
    private String name;

    public SalePointResponse() {
    }

    public SalePointResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
