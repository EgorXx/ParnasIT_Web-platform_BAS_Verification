package com.parnasit.bas.verification.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RouteResponse(
        UUID id,
        String name,
        String userFullName,
        String status,
        Instant createdAt,
        List<Coordinate> points
) {}
