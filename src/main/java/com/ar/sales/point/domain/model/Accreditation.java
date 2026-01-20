package com.ar.sales.point.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accreditation {
    private Long id;
    private SalePoint salePoint;
    private String salePointName;
    private Double amount;
    private Date acreditationDate;
}
