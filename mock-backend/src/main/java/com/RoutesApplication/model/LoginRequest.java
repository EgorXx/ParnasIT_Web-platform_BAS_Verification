package com.RoutesApplication.model;

public record LoginRequest(
        String email,
        String password
) {
}