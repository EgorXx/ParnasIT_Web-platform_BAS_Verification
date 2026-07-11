package com.parnasit.bas.verification.core.service;

import com.parnasit.bas.verification.core.dto.PointSpec;
import com.parnasit.bas.verification.persistence.entity.Route;
import com.parnasit.bas.verification.persistence.entity.RoutePoint;
import com.parnasit.bas.verification.persistence.entity.User;
import com.parnasit.bas.verification.persistence.enums.RouteStatus;
import com.parnasit.bas.verification.persistence.repository.RouteRepository;
import com.parnasit.bas.verification.persistence.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AutoCheckService autoCheckService;

    @InjectMocks
    private RouteService routeService;

    private final UUID userId = UUID.randomUUID();
    private final UUID routeId = UUID.randomUUID();

    @Test
    void autoApproval_shouldSetApprovedStatus() {
        when(autoCheckService.check(any())).thenReturn(true);
        when(userRepository.getReferenceById(userId)).thenReturn(new User());
        when(routeRepository.save(any(Route.class))).thenAnswer(inv -> inv.getArgument(0));

        Route route = routeService.createRoute(userId, "Маршрут 1",
                List.of(new PointSpec(55.75, 37.62), new PointSpec(55.76, 37.63)));

        assertEquals(RouteStatus.APPROVED, route.getStatus());
        assertTrue(route.isAutoCheckResult());
    }

    @Test
    void failedAutoCheck_shouldStaySubmittedAndAppearInPendingList() {
        when(autoCheckService.check(any())).thenReturn(false);
        when(userRepository.getReferenceById(userId)).thenReturn(new User());
        when(routeRepository.save(any(Route.class))).thenAnswer(inv -> inv.getArgument(0));

        Route route = routeService.createRoute(userId, "Маршрут 2",
                List.of(new PointSpec(55.75, 37.62), new PointSpec(56.00, 38.00)));

        assertEquals(RouteStatus.SUBMITTED, route.getStatus());
        assertFalse(route.isAutoCheckResult());
    }

    @Test
    void adminApprove_shouldChangeStatusToApproved() {
        Route route = createRoute(RouteStatus.SUBMITTED);
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class))).thenAnswer(inv -> inv.getArgument(0));

        Route result = routeService.approveRoute(routeId);

        assertEquals(RouteStatus.APPROVED, result.getStatus());
    }

    @Test
    void adminReject_shouldChangeStatusToRejected() {
        Route route = createRoute(RouteStatus.SUBMITTED);
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class))).thenAnswer(inv -> inv.getArgument(0));

        Route result = routeService.rejectRoute(routeId);

        assertEquals(RouteStatus.REJECTED, result.getStatus());
    }

    @Test
    void repeatedDecision_shouldThrowException() {
        Route route = createRoute(RouteStatus.APPROVED);
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));

        assertThrows(IllegalArgumentException.class, () -> routeService.approveRoute(routeId));
        assertThrows(IllegalArgumentException.class, () -> routeService.rejectRoute(routeId));
    }

    @Test
    void getRoute_wrongUser_shouldThrowException() {
        User owner = new User();
        owner.setId(UUID.randomUUID());
        Route route = createRoute(RouteStatus.SUBMITTED);
        route.setUser(owner);
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));

        UUID otherUserId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> routeService.getRoute(otherUserId, routeId));
    }

    private Route createRoute(RouteStatus status) {
        Route route = new Route();
        route.setId(routeId);
        route.setName("Тестовый маршрут");
        route.setStatus(status);
        route.setAutoCheckResult(status == RouteStatus.APPROVED);
        route.setPoints(List.of(new RoutePoint()));
        User owner = new User();
        owner.setId(userId);
        route.setUser(owner);
        return route;
    }
}
