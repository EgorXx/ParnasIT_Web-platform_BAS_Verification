package com.parnasit.bas.verification.api.integration;

import com.parnasit.bas.verification.api.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RouteIT extends BaseIntegrationTest {

    @Test
    void createRouteInsideZoneShouldAutoApprove() {
        String adminToken = adminToken();
        String userToken = registerAndToken("User A", "usera@test.com", "password123");

        CreateZoneRequest zone = new CreateZoneRequest("Test Zone", List.of(
                new Coordinate(56.0, 37.0),
                new Coordinate(56.0, 38.0),
                new Coordinate(55.0, 38.0),
                new Coordinate(55.0, 37.0),
                new Coordinate(56.0, 37.0)
        ));
        rest.postForEntity(url("/api/zones"), authorized(zone, adminToken), Void.class);

        CreateRouteRequest route = new CreateRouteRequest("Inside Zone Route", List.of(
                new Coordinate(55.5, 37.3),
                new Coordinate(55.7, 37.6)
        ));
        ResponseEntity<RouteResponse> response = rest.postForEntity(
                url("/api/routes"), authorized(route, userToken), RouteResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        RouteResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo("APPROVED");
        assertThat(body.name()).isEqualTo("Inside Zone Route");
        assertThat(body.userFullName()).isEqualTo("User A");
        assertThat(body.points()).hasSize(2);
        assertThat(body.id()).isNotNull();
        assertThat(body.createdAt()).isNotNull();
    }

    @Test
    void createRouteOutsideZonesShouldStaySubmitted() {
        String userToken = registerAndToken("User B", "userb@test.com", "password123");

        CreateRouteRequest route = new CreateRouteRequest("Outside Route", List.of(
                new Coordinate(60.0, 30.0),
                new Coordinate(61.0, 31.0)
        ));
        ResponseEntity<RouteResponse> response = rest.postForEntity(
                url("/api/routes"), authorized(route, userToken), RouteResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        RouteResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.status()).isEqualTo("SUBMITTED");
        assertThat(body.name()).isEqualTo("Outside Route");
        assertThat(body.points()).hasSize(2);
    }

    @Test
    void shouldListOwnRoutes() {
        String userToken = registerAndToken("User C", "userc@test.com", "password123");

        CreateRouteRequest r1 = new CreateRouteRequest("Route 1", List.of(
                new Coordinate(60.0, 30.0), new Coordinate(61.0, 31.0)));
        CreateRouteRequest r2 = new CreateRouteRequest("Route 2", List.of(
                new Coordinate(62.0, 32.0), new Coordinate(63.0, 33.0)));

        rest.postForEntity(url("/api/routes"), authorized(r1, userToken), RouteResponse.class);
        rest.postForEntity(url("/api/routes"), authorized(r2, userToken), RouteResponse.class);

        ResponseEntity<RouteResponse[]> response = rest.exchange(
                url("/api/routes"), org.springframework.http.HttpMethod.GET,
                authorized(null, userToken), RouteResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).extracting(RouteResponse::name)
                .containsExactlyInAnyOrder("Route 1", "Route 2");
    }

    @Test
    void shouldReturn404ForForeignRoute() {
        String tokenA = registerAndToken("User D", "userd@test.com", "password123");
        String tokenB = registerAndToken("User E", "usere@test.com", "password123");

        CreateRouteRequest route = new CreateRouteRequest("A's Route", List.of(
                new Coordinate(60.0, 30.0), new Coordinate(61.0, 31.0)));
        ResponseEntity<RouteResponse> created = rest.postForEntity(
                url("/api/routes"), authorized(route, tokenA), RouteResponse.class);
        UUID routeId = created.getBody().id();

        ResponseEntity<ErrorResponse> response = rest.exchange(
                url("/api/routes/" + routeId), org.springframework.http.HttpMethod.GET,
                authorized(null, tokenB), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
