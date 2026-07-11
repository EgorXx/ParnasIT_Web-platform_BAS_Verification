package com.parnasit.bas.verification.core.service;

import com.parnasit.bas.verification.persistence.entity.User;
import com.parnasit.bas.verification.persistence.entity.VerificationZone;
import com.parnasit.bas.verification.persistence.repository.UserRepository;
import com.parnasit.bas.verification.persistence.repository.VerificationZoneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZoneServiceTest {

    @Mock
    private VerificationZoneRepository zoneRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ZoneService zoneService;

    private final UUID userId = UUID.randomUUID();

    @Test
    void createZone_shouldSaveWithCorrectFields() {
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(zoneRepository.save(any(VerificationZone.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        List<Coordinate> coords = List.of(
                new Coordinate(37.62, 55.75),
                new Coordinate(37.63, 55.76),
                new Coordinate(37.64, 55.77),
                new Coordinate(37.62, 55.75)
        );

        VerificationZone zone = zoneService.createZone("Тестовая зона", coords, userId);

        assertEquals("Тестовая зона", zone.getName());
        assertEquals(user, zone.getCreatedBy());
        assertNotNull(zone.getCreatedAt());
        assertNotNull(zone.getGeometry());
        assertEquals(4326, zone.getGeometry().getSRID());
        assertInstanceOf(Polygon.class, zone.getGeometry());
    }

    @Test
    void createZone_openPolygon_shouldThrowException() {
        List<Coordinate> coords = List.of(
                new Coordinate(37.62, 55.75),
                new Coordinate(37.63, 55.76),
                new Coordinate(37.64, 55.77)  // не совпадает с первой
        );

        assertThrows(IllegalArgumentException.class,
                () -> zoneService.createZone("Зона", coords, userId));
    }

    @Test
    void createZone_userNotFound_shouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        List<Coordinate> coords = List.of(
                new Coordinate(37.62, 55.75),
                new Coordinate(37.63, 55.76),
                new Coordinate(37.62, 55.75)
        );

        assertThrows(EntityNotFoundException.class,
                () -> zoneService.createZone("Зона", coords, userId));
    }

    @Test
    void getAllZones_shouldReturnList() {
        VerificationZone zone1 = new VerificationZone();
        zone1.setName("Зона 1");
        VerificationZone zone2 = new VerificationZone();
        zone2.setName("Зона 2");
        when(zoneRepository.findAll()).thenReturn(List.of(zone1, zone2));

        List<VerificationZone> zones = zoneService.getAllZones();

        assertEquals(2, zones.size());
        assertEquals("Зона 1", zones.get(0).getName());
    }

    @Test
    void getAllZones_empty_shouldReturnEmptyList() {
        when(zoneRepository.findAll()).thenReturn(List.of());

        List<VerificationZone> zones = zoneService.getAllZones();

        assertTrue(zones.isEmpty());
    }
}
