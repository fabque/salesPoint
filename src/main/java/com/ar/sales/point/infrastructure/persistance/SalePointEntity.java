package com.ar.sales.point.infrastructure.persistance;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sale_points")
public class SalePointEntity {

    @Id
    private Long id;

    private String name;

    public SalePointEntity() {
    }

    public SalePointEntity(Long id, String name) {
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
