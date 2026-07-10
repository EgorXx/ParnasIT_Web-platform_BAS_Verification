package com.parnasit.bas.verification.persistence.repository;

import com.parnasit.bas.verification.persistence.entity.RoutePoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutePointRepository extends JpaRepository<RoutePoint, Long> {
}
