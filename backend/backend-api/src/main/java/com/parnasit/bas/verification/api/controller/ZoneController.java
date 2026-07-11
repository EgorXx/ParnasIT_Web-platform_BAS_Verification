package com.parnasit.bas.verification.api.controller;

import com.parnasit.bas.verification.api.dto.Coordinate;
import com.parnasit.bas.verification.api.dto.CreateZoneRequest;
import com.parnasit.bas.verification.api.dto.ZoneResponse;
import com.parnasit.bas.verification.core.service.ZoneService;
import com.parnasit.bas.verification.persistence.entity.VerificationZone;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    public ResponseEntity<ZoneResponse> create(@Valid @RequestBody CreateZoneRequest request) {
        List<org.locationtech.jts.geom.Coordinate> jtsCoords = request.coordinates().stream()
                .map(c -> new org.locationtech.jts.geom.Coordinate(c.lng(), c.lat()))
                .toList();

        UUID userId = getCurrentUserId();
        VerificationZone zone = zoneService.createZone(request.name(), jtsCoords, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(zone));
    }

    @GetMapping
    public ResponseEntity<List<ZoneResponse>> getAll() {
        return ResponseEntity.ok(
                zoneService.getAllZones().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private ZoneResponse toResponse(VerificationZone zone) {
        org.locationtech.jts.geom.Coordinate[] jtsCoords = zone.getGeometry().getExteriorRing().getCoordinates();
        List<Coordinate> coordinates = Arrays.stream(jtsCoords)
                .map(c -> new Coordinate(c.y, c.x))
                .toList();

        return new ZoneResponse(
                zone.getId(),
                zone.getName(),
                coordinates,
                zone.getCreatedAt()
        );
    }

    private UUID getCurrentUserId() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(userId);
    }
}
