package com.parnasit.bas.verification.api.controller;

import com.parnasit.bas.verification.api.dto.Coordinate;
import com.parnasit.bas.verification.api.dto.CreateRouteRequest;
import com.parnasit.bas.verification.api.dto.RouteResponse;
import com.parnasit.bas.verification.core.dto.PointSpec;
import com.parnasit.bas.verification.core.service.RouteService;
import com.parnasit.bas.verification.persistence.entity.Route;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<RouteResponse> create(@Valid @RequestBody CreateRouteRequest request) {
        List<PointSpec> points = request.points().stream()
                .map(p -> new PointSpec(p.lat(), p.lng()))
                .toList();

        UUID userId = getCurrentUserId();
        Route route = routeService.createRoute(userId, request.name(), points);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(route));
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> list() {
        UUID userId = getCurrentUserId();
        List<RouteResponse> routes = routeService.getRoutes(userId).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> get(@PathVariable UUID id) {
        UUID userId = getCurrentUserId();
        Route route = routeService.getRoute(userId, id);
        return ResponseEntity.ok(toResponse(route));
    }

    private UUID getCurrentUserId() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(userId);
    }

    private RouteResponse toResponse(Route route) {
        List<Coordinate> points = route.getPoints().stream()
                .map(p -> new Coordinate(p.getLatitude(), p.getLongitude()))
                .toList();
        return new RouteResponse(
                route.getId(),
                route.getName(),
                route.getUser().getFullName(),
                route.getStatus().name(),
                route.getCreatedAt(),
                points
        );
    }
}
