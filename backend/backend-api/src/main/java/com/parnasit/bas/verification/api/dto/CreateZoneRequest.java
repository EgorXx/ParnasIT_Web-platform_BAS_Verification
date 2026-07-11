package com.parnasit.bas.verification.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateZoneRequest(
        @NotBlank String name,
        @Size(min = 4) List<Coordinate> coordinates
) {}
