package com.parnasit.bas.verification.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateRouteRequest(
        @NotBlank(message = "Название маршрута обязательно") String name,
        @Valid @Size(min = 2, message = "Маршрут должен содержать минимум 2 точки")
        List<Coordinate> points
) {}
