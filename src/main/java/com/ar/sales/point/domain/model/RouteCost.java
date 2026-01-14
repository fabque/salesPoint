package com.ar.sales.point.domain.model;

import java.util.List;

public class RouteCost{
    private List<Long> path;
    private double cost;

    public RouteCost(List<Long> path, double cost) {
        this.path = path;
        this.cost = cost;
    }
}
