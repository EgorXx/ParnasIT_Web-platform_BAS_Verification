package com.parnasit.bas.verification.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ZoneResponse(
        UUID id,
        String name,
        List<Coordinate> coordinates,
        Instant createdAt
) {}
