package com.RoutesApplication.model;

import lombok.Data;

@Data
public class User {

    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String role;
}