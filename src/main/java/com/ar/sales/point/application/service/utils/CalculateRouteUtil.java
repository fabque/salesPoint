package com.ar.sales.point.application.service.utils;

import com.ar.sales.point.domain.model.RouteCost;
import com.ar.sales.point.domain.model.SalePoint;
import com.ar.sales.point.domain.model.SalePointCost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class CalculateRouteUtil {

    public static RouteCost getOptimalRoute(SalePoint origin, SalePoint destiny, List<SalePoint> stationList, List<SalePointCost> routeList) {
        if (routeList.isEmpty()) {
            List<Long> result = new ArrayList<>();
            return RouteCost.builder().path(result).cost(0).build();
        }
        Map<SalePoint, Double> costs = new HashMap<>();
        Map<SalePoint, SalePoint> predecesors = new HashMap<>();
        PriorityQueue<SalePoint> queue = new PriorityQueue<>(Comparator.comparingDouble(costs::get));

        stationList.forEach(station -> {
            costs.put(station, Double.MAX_VALUE);
        });
        costs.put(origin, (double) 0);

        queue.add(origin);

        while (!queue.isEmpty()) {
            SalePoint actual = queue.poll();

            if (actual.equals(destiny)) {
                break;
            }

            for (SalePointCost route : routeList) {
                /**
                 * Examine origen and destination ways (bidirectional)
                 */
                if (route.getSalePointOrigin().equals(actual) || route.getSalePointDestination().equals(actual)) {
                    SalePoint neighbour = (route.getSalePointOrigin().equals(actual)) ? route.getSalePointDestination() : route.getSalePointOrigin();
                    double newCost = costs.get(actual) + route.getCost();

                    if (newCost < costs.get(neighbour)) {
                        costs.put(neighbour, newCost);
                        predecesors.put(neighbour, actual);
                        queue.add(neighbour);
                    }
                }
            }
        }

        List<Long> path = new LinkedList<>();
        SalePoint step = destiny;

        if (predecesors.get(step) == null) {
            return null;
        }

        while (step != null) {
            path.add(0, step.id());
            step = predecesors.get(step);
        }

        return RouteCost.builder().path(path).cost(costs.get(destiny)).build();
    }
}
