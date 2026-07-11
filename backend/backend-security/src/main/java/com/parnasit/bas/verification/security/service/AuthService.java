package com.parnasit.bas.verification.security.service;

import com.parnasit.bas.verification.persistence.entity.User;
import com.parnasit.bas.verification.persistence.enums.UserRole;
import com.parnasit.bas.verification.persistence.repository.UserRepository;
import com.parnasit.bas.verification.security.dto.LoginResult;
import com.parnasit.bas.verification.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void register(String fullName, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email уже зарегистрирован");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(UserRole.USER);

        userRepository.save(user);
    }

    public LoginResult login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Неверный email или пароль"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Неверный email или пароль");
        }

        String token = jwtProvider.generate(user.getId(), user.getEmail(), user.getRole());

        return new LoginResult(token, user.getRole().name());
    }
}
