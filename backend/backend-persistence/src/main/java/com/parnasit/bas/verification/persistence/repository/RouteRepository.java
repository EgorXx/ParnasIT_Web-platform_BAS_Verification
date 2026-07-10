package com.parnasit.bas.verification.persistence.repository;

import com.parnasit.bas.verification.persistence.entity.Route;
import com.parnasit.bas.verification.persistence.enums.RouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, UUID> {
    List<Route> findByUserId(UUID userId);
}
