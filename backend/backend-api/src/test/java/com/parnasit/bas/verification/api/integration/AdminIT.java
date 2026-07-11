package com.parnasit.bas.verification.api.integration;

import com.parnasit.bas.verification.api.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AdminIT extends BaseIntegrationTest {

    @Test
    void shouldCreateZoneAndPersistGeometry() {
        String adminToken = adminToken();

        CreateZoneRequest zone = new CreateZoneRequest("Adm Zone", List.of(
                new Coordinate(56.0, 37.0),
                new Coordinate(56.0, 38.0),
                new Coordinate(55.0, 38.0),
                new Coordinate(55.0, 37.0),
                new Coordinate(56.0, 37.0)
        ));
        ResponseEntity<ZoneResponse> response = rest.postForEntity(
                url("/api/zones"), authorized(zone, adminToken), ZoneResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ZoneResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.name()).isEqualTo("Adm Zone");
        assertThat(body.coordinates()).hasSize(5);
        assertThat(body.id()).isNotNull();
        assertThat(body.createdAt()).isNotNull();

        ResponseEntity<ZoneResponse[]> allResponse = rest.exchange(
                url("/api/zones"), HttpMethod.GET,
                authorized(null, adminToken), ZoneResponse[].class);

        assertThat(allResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allResponse.getBody()).anyMatch(z -> z.name().equals("Adm Zone"));
    }

    @Test
    void shouldApprovePendingRoute() {
        String adminToken = adminToken();
        String userToken = registerAndToken("User F", "userf@test.com", "password123");

        CreateRouteRequest route = new CreateRouteRequest("Pending Route", List.of(
                new Coordinate(60.0, 30.0), new Coordinate(61.0, 31.0)));
        ResponseEntity<RouteResponse> created = rest.postForEntity(
                url("/api/routes"), authorized(route, userToken), RouteResponse.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody().status()).isEqualTo("SUBMITTED");
        UUID routeId = created.getBody().id();

        ResponseEntity<RouteResponse[]> pendingResponse = rest.exchange(
                url("/api/admin/routes/pending"), HttpMethod.GET,
                authorized(null, adminToken), RouteResponse[].class);
        assertThat(pendingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pendingResponse.getBody()).anyMatch(r -> r.id().equals(routeId));

        ResponseEntity<RouteResponse> approved = rest.exchange(
                url("/api/admin/routes/" + routeId + "/approve"), HttpMethod.POST,
                authorized(null, adminToken), RouteResponse.class);

        assertThat(approved.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(approved.getBody().status()).isEqualTo("APPROVED");
    }

    @Test
    void shouldRejectPendingRoute() {
        String adminToken = adminToken();
        String userToken = registerAndToken("User G", "userg@test.com", "password123");

        CreateRouteRequest route = new CreateRouteRequest("Reject Route", List.of(
                new Coordinate(60.0, 30.0), new Coordinate(61.0, 31.0)));
        ResponseEntity<RouteResponse> created = rest.postForEntity(
                url("/api/routes"), authorized(route, userToken), RouteResponse.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody().status()).isEqualTo("SUBMITTED");
        UUID routeId = created.getBody().id();

        ResponseEntity<RouteResponse> rejected = rest.exchange(
                url("/api/admin/routes/" + routeId + "/reject"), HttpMethod.POST,
                authorized(null, adminToken), RouteResponse.class);

        assertThat(rejected.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rejected.getBody().status()).isEqualTo("REJECTED");
    }
}
