package com.parnasit.bas.verification.api.integration;

import com.parnasit.bas.verification.api.dto.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityIT extends BaseIntegrationTest {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Test
    void shouldReturn401WithoutToken() {
        ResponseEntity<ErrorResponse> response = rest.getForEntity(
                url("/api/routes"), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturn401ForExpiredToken() {
        String expiredToken = buildExpiredToken();

        ResponseEntity<ErrorResponse> response = rest.exchange(
                url("/api/routes"), HttpMethod.GET,
                authorized(null, expiredToken), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturn403WhenUserAccessesAdminEndpoint() {
        String userToken = registerAndToken("User H", "userh@test.com", "password123");

        ResponseEntity<ErrorResponse> response = rest.exchange(
                url("/api/admin/routes/pending"), HttpMethod.GET,
                authorized(null, userToken), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldReturn403WhenUserCreatesZone() {
        String userToken = registerAndToken("User I", "useri@test.com", "password123");

        CreateZoneRequest zone = new CreateZoneRequest("Hack", List.of(
                new Coordinate(56.0, 37.0), new Coordinate(56.0, 38.0),
                new Coordinate(55.0, 38.0), new Coordinate(55.0, 37.0),
                new Coordinate(56.0, 37.0)
        ));
        ResponseEntity<ErrorResponse> response = rest.postForEntity(
                url("/api/zones"), authorized(zone, userToken), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldReturn404ForAnotherUsersRoute() {
        String tokenA = registerAndToken("User J", "userj@test.com", "password123");
        String tokenB = registerAndToken("User K", "userk@test.com", "password123");

        CreateRouteRequest route = new CreateRouteRequest("J's Route", List.of(
                new Coordinate(60.0, 30.0), new Coordinate(61.0, 31.0)));
        ResponseEntity<RouteResponse> created = rest.postForEntity(
                url("/api/routes"), authorized(route, tokenA), RouteResponse.class);
        UUID routeId = created.getBody().id();

        ResponseEntity<ErrorResponse> response = rest.exchange(
                url("/api/routes/" + routeId), HttpMethod.GET,
                authorized(null, tokenB), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String buildExpiredToken() {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
        return Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .claim("email", "expired@test.com")
                .claim("role", "USER")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() - 3600_000))
                .signWith(key)
                .compact();
    }
}
