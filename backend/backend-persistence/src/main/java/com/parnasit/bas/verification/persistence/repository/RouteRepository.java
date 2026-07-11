package com.parnasit.bas.verification.persistence.repository;

import com.parnasit.bas.verification.persistence.entity.Route;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, UUID> {

    @EntityGraph(attributePaths = {"points"})
    List<Route> findByUserId(UUID userId);

    @Override
    @EntityGraph(attributePaths = {"points", "user"})
    Optional<Route> findById(UUID id);
}
