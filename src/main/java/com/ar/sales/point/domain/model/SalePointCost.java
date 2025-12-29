package com.ar.sales.point.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalePointCost {
    private Long id;
    private SalePoint salePointOrigin;
    private SalePoint salePointDestination;
    private Double cost;
}
