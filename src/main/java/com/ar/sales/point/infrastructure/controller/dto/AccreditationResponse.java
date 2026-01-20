package com.ar.sales.point.infrastructure.controller.dto;

import java.util.Date;

public record AccreditationResponse(Long id, Long salePointId, String salePointName, Double amount, Date acreditationDate) {

}
