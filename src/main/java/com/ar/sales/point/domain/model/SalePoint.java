package com.ar.sales.point.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SalePoint {
    private final Long id;
    private final String name;
}


