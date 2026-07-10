package com.RoutesApplication.user;

public record LoginRequest(
        String email,
        String password
) {
}