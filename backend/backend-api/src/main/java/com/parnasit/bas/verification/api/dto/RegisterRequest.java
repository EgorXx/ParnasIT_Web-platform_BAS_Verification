package com.parnasit.bas.verification.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "ФИО обязательно") String fullName,
        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email") String email,
        @NotBlank(message = "Пароль обязателен")
        @Size(min = 6, message = "Пароль должен содержать минимум 6 символов") String password
) {}
