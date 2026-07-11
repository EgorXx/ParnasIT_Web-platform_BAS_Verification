package com.parnasit.bas.verification.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateRouteRequest(
        @NotBlank String name,
        @Valid @Size(min = 2, message = "Route must have at least 2 points")
        List<Coordinate> points
) {}
