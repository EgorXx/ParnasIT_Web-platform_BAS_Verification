package com.parnasit.bas.verification.api.controller;

import com.parnasit.bas.verification.api.dto.AuthResponse;
import com.parnasit.bas.verification.api.dto.LoginRequest;
import com.parnasit.bas.verification.api.dto.RegisterRequest;
import com.parnasit.bas.verification.security.dto.LoginResult;
import com.parnasit.bas.verification.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request.fullName(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = authService.login(request.email(), request.password());
        return ResponseEntity.ok(new AuthResponse(result.token(), result.role()));
    }
}
