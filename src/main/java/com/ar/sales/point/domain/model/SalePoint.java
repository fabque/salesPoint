package com.ar.sales.point.domain.model;

public record SalePoint(Long id, String name) {

    public SalePoint(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
