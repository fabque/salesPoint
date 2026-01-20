package com.ar.sales.point.infrastructure.persistence.postgres;

import com.ar.sales.point.domain.model.Accreditation;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;
import com.ar.sales.point.infrastructure.persistence.h2.SalePointCostEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "accreditations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccreditationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long salePointId;
    private String salePointName;
    private Double amount;
    private Date acreditationDate;

    public AccreditationEntity(Long salePointId, String salePointName, Double amount, Date acreditationDate){
        this.salePointId = salePointId;
        this.salePointName = salePointName;
        this.amount = amount;
        this.acreditationDate = acreditationDate;
    }
}
