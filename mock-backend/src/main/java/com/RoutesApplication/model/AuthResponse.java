package com.RoutesApplication.model;

public record AuthResponse(
        String token,
        String role
) {
}