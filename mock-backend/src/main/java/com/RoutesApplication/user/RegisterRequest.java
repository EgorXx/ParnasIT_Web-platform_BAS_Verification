package com.RoutesApplication.user;

public record RegisterRequest(
        String fullName,
        String email,
        String password
) {
}