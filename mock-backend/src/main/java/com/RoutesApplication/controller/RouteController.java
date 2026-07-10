package com.RoutesApplication.controller;


import com.RoutesApplication.model.Route;
import com.RoutesApplication.model.RouteSummary;
import com.RoutesApplication.repository.RouteRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {


    private final RouteRepository repository;


    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody Route route
    ) {

        Route saved = repository.save(route);

        return ResponseEntity
                .created(
                        URI.create("/api/routes/" + saved.getId())
                )
                .build();
    }


    @GetMapping
    public List<RouteSummary> getAll() {

        return repository.findAll()
                .stream()
                .map(route ->
                        new RouteSummary(
                                route.getId(),
                                route.getName()
                        )
                )
                .toList();

    }


    @GetMapping("/{id}")
    public ResponseEntity<Route> getById(
            @PathVariable Long id
    ) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );

    }

}