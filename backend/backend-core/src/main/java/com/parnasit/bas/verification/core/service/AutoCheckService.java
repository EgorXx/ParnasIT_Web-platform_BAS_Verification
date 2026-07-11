package com.parnasit.bas.verification.core.service;

import com.parnasit.bas.verification.persistence.entity.RoutePoint;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoCheckService {

    private final EntityManager entityManager;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional(readOnly = true)
    public boolean check(List<RoutePoint> points) {
        if (points.isEmpty()) {
            return false;
        }

        LineString line = buildLine(points);

        Long count = entityManager.createQuery(
                        "SELECT COUNT(vz) FROM VerificationZone vz WHERE covers(vz.geometry, :line) = true",
                        Long.class)
                .setParameter("line", line)
                .getSingleResult();

        return count > 0;
    }

    private LineString buildLine(List<RoutePoint> points) {
        Coordinate[] coords = points.stream()
                .sorted(Comparator.comparingInt(RoutePoint::getOrderIndex))
                .map(p -> new Coordinate(p.getLongitude(), p.getLatitude()))
                .toArray(Coordinate[]::new);
        LineString line = geometryFactory.createLineString(coords);
        line.setSRID(4326);
        return line;
    }
}
