package com.parnasit.bas.verification.security.postconstruct;

import com.parnasit.bas.verification.persistence.entity.User;
import com.parnasit.bas.verification.persistence.enums.UserRole;
import com.parnasit.bas.verification.persistence.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@RequiredArgsConstructor
@Component
public class AdminUserCreator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAdminAccount() {
        String fullName = "Хуззятов Камиль Артурович";
        String email = "kkhuzzyatov@gmail.com";
        String password = "kkhuzzyatov@gmail.com";

        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setRole(UserRole.ADMIN);
            user.setCreatedAt(Instant.now());

            userRepository.save(user);
        }
    }
}
