package com.RoutesApplication.route;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RouteRepository {

    private final List<Route> routes = new ArrayList<>();

    private long sequence = 1;


    public Route save(Route route) {

        route.setId(sequence++);
        routes.add(route);

        return route;
    }


    public List<Route> findAll() {

        return routes;

    }


    public Optional<Route> findById(Long id) {

        return routes.stream()
                .filter(route -> route.getId().equals(id))
                .findFirst();

    }

}