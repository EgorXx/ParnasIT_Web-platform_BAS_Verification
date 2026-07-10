package com.RoutesApplication.user;

public record AuthResponse(
        String token,
        String role
) {
}