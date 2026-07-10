package com.RoutesApplication.controller;

import com.RoutesApplication.model.*;
import com.RoutesApplication.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {


        User user = new User();

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole("USER");


        userRepository.save(user);


        return ResponseEntity.ok(
                new AuthResponse(
                        generateToken(),
                        user.getRole()
                )
        );
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {

        User user = userRepository.findByEmail(request.email())
                .orElse(null);


        return ResponseEntity.ok(
                new AuthResponse(
                        generateToken(),
                        user.getRole()
                )
        );
    }


    private String generateToken() {

        return UUID.randomUUID().toString();
    }

}