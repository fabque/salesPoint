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

    public Accreditation (SalePoint salePoint, Double amount, Date acreditationDate) {
        this.salePoint = salePoint;
        this.salePointName = salePoint.getName();
        this.amount = amount;
        this.acreditationDate = acreditationDate;
    }
}
