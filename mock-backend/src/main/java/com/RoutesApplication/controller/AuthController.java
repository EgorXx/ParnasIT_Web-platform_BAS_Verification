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
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {

        if (userRepository.findByEmail(request.email()).isPresent()) {

            return ResponseEntity
                    .badRequest()
                    .body("Email already exists");
        }


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
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        User user = userRepository.findByEmail(request.email())
                .orElse(null);


        if (user == null ||
                !user.getPassword().equals(request.password())) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid credentials");
        }


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