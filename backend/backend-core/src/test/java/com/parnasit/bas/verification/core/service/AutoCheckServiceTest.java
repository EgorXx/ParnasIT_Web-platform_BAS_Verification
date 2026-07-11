package com.parnasit.bas.verification.core.service;

import com.parnasit.bas.verification.persistence.entity.RoutePoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutoCheckServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> typedQuery;

    private AutoCheckService autoCheckService;

    @BeforeEach
    void setUp() {
        autoCheckService = new AutoCheckService(entityManager);
        lenient().when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
        lenient().when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
    }

    @Test
    void allPointsAndLineInsideOneZone_shouldApprove() {
        when(typedQuery.getSingleResult()).thenReturn(1L);

        boolean result = autoCheckService.check(createPoints(55.75, 37.62, 55.76, 37.63));

        assertTrue(result);
    }

    @Test
    void pointsInDifferentZones_shouldNotApprove() {
        when(typedQuery.getSingleResult()).thenReturn(0L);

        boolean result = autoCheckService.check(createPoints(55.75, 37.62, 55.80, 37.70));

        assertFalse(result);
    }

    @Test
    void pointsInsideButLineOutside_shouldNotApprove() {
        when(typedQuery.getSingleResult()).thenReturn(0L);

        boolean result = autoCheckService.check(createPoints(55.75, 37.62, 55.76, 37.70));

        assertFalse(result);
    }

    @Test
    void onePointOutside_shouldNotApprove() {
        when(typedQuery.getSingleResult()).thenReturn(0L);

        boolean result = autoCheckService.check(createPoints(55.75, 37.62, 56.00, 37.00));

        assertFalse(result);
    }

    @Test
    void allPointsOutside_shouldNotApprove() {
        when(typedQuery.getSingleResult()).thenReturn(0L);

        boolean result = autoCheckService.check(createPoints(56.00, 38.00, 56.01, 38.01));

        assertFalse(result);
    }

    @Test
    void pointOnBoundary_shouldApprove() {
        when(typedQuery.getSingleResult()).thenReturn(1L);

        boolean result = autoCheckService.check(createPoints(55.75, 37.62, 55.76, 37.63));

        assertTrue(result);
    }

    @Test
    void lineOnBoundary_shouldApprove() {
        when(typedQuery.getSingleResult()).thenReturn(1L);

        boolean result = autoCheckService.check(createPoints(55.75, 37.62, 55.76, 37.63));

        assertTrue(result);
    }

    @Test
    void noZonesInSystem_shouldNotApprove() {
        when(typedQuery.getSingleResult()).thenReturn(0L);

        boolean result = autoCheckService.check(createPoints(55.75, 37.62, 55.76, 37.63));

        assertFalse(result);
    }

    @Test
    void routeInsideOneOfSeveralZones_shouldApprove() {
        when(typedQuery.getSingleResult()).thenReturn(1L);

        boolean result = autoCheckService.check(createPoints(55.75, 37.62, 55.76, 37.63));

        assertTrue(result);
    }

    @Test
    void emptyPoints_shouldReturnFalse() {
        boolean result = autoCheckService.check(Collections.emptyList());

        assertFalse(result);
    }

    private List<RoutePoint> createPoints(double lat1, double lng1, double lat2, double lng2) {
        RoutePoint p1 = new RoutePoint();
        p1.setOrderIndex(0);
        p1.setLatitude(lat1);
        p1.setLongitude(lng1);

        RoutePoint p2 = new RoutePoint();
        p2.setOrderIndex(1);
        p2.setLatitude(lat2);
        p2.setLongitude(lng2);

        return List.of(p1, p2);
    }
}
