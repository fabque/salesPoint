package com.ar.sales.point.infrastructure.controller.dto;

import java.util.Date;

public record AccreditationRequest(Long salePointId, String salePointName, Double amount, Date acreditationDate) {
}
