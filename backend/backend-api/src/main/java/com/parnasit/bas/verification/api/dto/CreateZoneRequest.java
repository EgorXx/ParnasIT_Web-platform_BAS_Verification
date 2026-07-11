package com.parnasit.bas.verification.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateZoneRequest(
        @NotBlank(message = "Название зоны обязательно") String name,
        @Size(min = 3, message = "Полигон должен содержать минимум 3 координаты")
        List<Coordinate> coordinates
) {}
