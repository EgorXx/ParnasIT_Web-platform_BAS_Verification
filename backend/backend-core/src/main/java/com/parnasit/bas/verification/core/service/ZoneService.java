package com.parnasit.bas.verification.core.service;

import com.parnasit.bas.verification.persistence.entity.User;
import com.parnasit.bas.verification.persistence.entity.VerificationZone;
import com.parnasit.bas.verification.persistence.repository.UserRepository;
import com.parnasit.bas.verification.persistence.repository.VerificationZoneRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final VerificationZoneRepository zoneRepository;
    private final UserRepository userRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public VerificationZone createZone(String name, List<Coordinate> coordinates, UUID userId) {
        Coordinate first = coordinates.getFirst();
        Coordinate last = coordinates.getLast();
        if (first.x != last.x || first.y != last.y) {
            throw new IllegalArgumentException("Первая и последняя координаты должны совпадать для замыкания полигона");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        Polygon polygon = geometryFactory.createPolygon(
                geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0])));
        polygon.setSRID(4326);

        VerificationZone zone = new VerificationZone();
        zone.setName(name);
        zone.setGeometry(polygon);
        zone.setCreatedBy(user);
        zone.setCreatedAt(Instant.now());

        return zoneRepository.save(zone);
    }

    @Transactional(readOnly = true)
    public List<VerificationZone> getAllZones() {
        return zoneRepository.findAll();
    }
}
