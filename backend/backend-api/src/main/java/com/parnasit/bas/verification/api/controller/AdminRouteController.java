package com.parnasit.bas.verification.api.controller;

import com.parnasit.bas.verification.api.dto.Coordinate;
import com.parnasit.bas.verification.api.dto.RouteResponse;
import com.parnasit.bas.verification.core.service.RouteService;
import com.parnasit.bas.verification.persistence.entity.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/routes")
@RequiredArgsConstructor
public class AdminRouteController {

    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<List<RouteResponse>> all() {
        List<RouteResponse> routes = routeService.getAllRoutes().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<RouteResponse>> pending() {
        List<RouteResponse> routes = routeService.getPendingRoutes().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(routes);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<RouteResponse> approve(@PathVariable UUID id) {
        Route route = routeService.approveRoute(id);
        return ResponseEntity.ok(toResponse(route));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<RouteResponse> reject(@PathVariable UUID id) {
        Route route = routeService.rejectRoute(id);
        return ResponseEntity.ok(toResponse(route));
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
                route.isAutoCheckResult(),
                route.getCreatedAt(),
                points
        );
    }
}
