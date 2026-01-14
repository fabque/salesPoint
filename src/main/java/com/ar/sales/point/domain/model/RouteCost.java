package com.ar.sales.point.domain.model;

import java.util.List;

public record RouteCost(List<Long> path, double cost) {
}
