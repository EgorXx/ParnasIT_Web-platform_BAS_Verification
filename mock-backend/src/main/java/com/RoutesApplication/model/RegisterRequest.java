package com.RoutesApplication.model;

public record RegisterRequest(
        String fullName,
        String email,
        String password
) {
}