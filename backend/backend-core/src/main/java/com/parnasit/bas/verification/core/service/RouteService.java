package com.parnasit.bas.verification.core.service;

import com.parnasit.bas.verification.core.dto.PointSpec;
import com.parnasit.bas.verification.persistence.entity.Route;
import com.parnasit.bas.verification.persistence.entity.RoutePoint;
import com.parnasit.bas.verification.persistence.entity.User;
import com.parnasit.bas.verification.persistence.enums.RouteStatus;
import com.parnasit.bas.verification.persistence.repository.RouteRepository;
import com.parnasit.bas.verification.persistence.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final AutoCheckService autoCheckService;

    @Transactional
    public Route createRoute(UUID userId, String name, List<PointSpec> points) {
        User user = userRepository.getReferenceById(userId);
        user.getFullName();

        Route route = new Route();
        route.setUser(user);
        route.setName(name);
        route.setStatus(RouteStatus.SUBMITTED);
        route.setAutoCheckResult(false);
        route.setCreatedAt(Instant.now());

        List<RoutePoint> routePoints = new ArrayList<>();
        int index = 0;
        for (PointSpec point : points) {
            RoutePoint rp = new RoutePoint();
            rp.setRoute(route);
            rp.setOrderIndex(index++);
            rp.setLatitude(point.latitude());
            rp.setLongitude(point.longitude());
            routePoints.add(rp);
        }
        route.setPoints(routePoints);

        boolean passed = autoCheckService.check(route.getPoints());
        route.setAutoCheckResult(passed);
        route.setStatus(passed ? RouteStatus.APPROVED : RouteStatus.SUBMITTED);

        return routeRepository.save(route);
    }

    @Transactional(readOnly = true)
    public List<Route> getRoutes(UUID userId) {
        return routeRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Route getRoute(UUID userId, UUID routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Маршрут не найден"));
        if (!route.getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("Маршрут не найден");
        }
        return route;
    }

    @Transactional(readOnly = true)
    public List<Route> getPendingRoutes() {
        return routeRepository.findByAutoCheckResultFalseAndStatus(RouteStatus.SUBMITTED);
    }

    @Transactional(readOnly = true)
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @Transactional
    public Route approveRoute(UUID routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Маршрут не найден"));
        if (route.getStatus() != RouteStatus.SUBMITTED) {
            throw new IllegalArgumentException("Маршрут уже обработан");
        }
        route.setStatus(RouteStatus.APPROVED);
        return routeRepository.save(route);
    }

    @Transactional
    public Route rejectRoute(UUID routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Маршрут не найден"));
        if (route.getStatus() != RouteStatus.SUBMITTED) {
            throw new IllegalArgumentException("Маршрут уже обработан");
        }
        route.setStatus(RouteStatus.REJECTED);
        return routeRepository.save(route);
    }
}
