package com.parnasit.bas.verification.api.integration;

import com.parnasit.bas.verification.api.dto.AuthResponse;
import com.parnasit.bas.verification.api.dto.LoginRequest;
import com.parnasit.bas.verification.api.dto.RegisterRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    static final PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>(
                DockerImageName.parse("postgis/postgis:16-3.4").asCompatibleSubstituteFor("postgres"))
                .withDatabaseName("bas_verification_test")
                .withUsername("test_user")
                .withPassword("test_pass");
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    protected int port;

    protected final RestTemplate rest = new RestTemplate();

    {
        rest.setErrorHandler(response -> false);
    }

    protected String token(String email, String password) {
        HttpEntity<LoginRequest> request = new HttpEntity<>(new LoginRequest(email, password));
        ResponseEntity<AuthResponse> response =
                rest.postForEntity(url("/api/auth/login"), request, AuthResponse.class);
        AuthResponse body = response.getBody();
        if (body == null || body.token() == null) {
            throw new IllegalStateException("Login failed for " + email
                    + ": status=" + response.getStatusCode());
        }
        return body.token();
    }

    protected String registerAndToken(String fullName, String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> request = new HttpEntity<>(new RegisterRequest(fullName, email, password));
        ResponseEntity<Void> response = rest.postForEntity(url("/api/auth/register"), request, Void.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Registration failed: status=" + response.getStatusCode());
        }
        return token(email, password);
    }

    protected String adminToken() {
        return token("admin@bas.ru", "admin");
    }

    protected <T> HttpEntity<T> authorized(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        if (body != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return new HttpEntity<>(body, headers);
    }

    protected String url(String path) {
        return "http://localhost:" + port + path;
    }
}
