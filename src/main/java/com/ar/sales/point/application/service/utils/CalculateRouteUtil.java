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
            return new RouteCost(result, 0);
        }
        Map<Long, Double> costs = new HashMap<>();
        Map<Long, Long> predecesors = new HashMap<>();
        PriorityQueue<Long> queue = new PriorityQueue<>(Comparator.comparingDouble(costs::get));

        stationList.forEach(station -> {
            costs.put(station.getId(), Double.MAX_VALUE);
        });
        costs.put(origin.getId(), (double) 0);

        queue.add(origin.getId());

        while (!queue.isEmpty()) {
            Long actual = queue.poll();

            if (actual.equals(destiny.getId())) {
                break;
            }

            for (SalePointCost route : routeList) {
                /**
                 * Examine origen and destination ways (bidirectional)
                 */
                if (route.getSalePointOrigin().getId().equals(actual) || route.getSalePointDestination().getId().equals(actual)) {
                    SalePoint neighbour = (route.getSalePointOrigin().getId().equals(actual)) ? route.getSalePointDestination() : route.getSalePointOrigin();
                    double newCost = costs.get(actual) + route.getCost();

                    if (newCost < costs.get(neighbour.getId())) {
                        costs.put(neighbour.getId(), newCost);
                        predecesors.put(neighbour.getId(), actual);
                        queue.add(neighbour.getId());
                    }
                }
            }
        }

        List<Long> path = new LinkedList<>();
        Long step = destiny.getId();

        if (predecesors.get(step) == null) {
            return null;
        }

        while (step != null) {
            path.add(0, step);
            step = predecesors.get(step);
        }

        return new RouteCost(path, costs.get(destiny.getId()));

    }
}
