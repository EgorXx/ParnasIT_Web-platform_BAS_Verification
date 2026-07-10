package com.parnasit.bas.verification.persistence.repository;

import com.parnasit.bas.verification.persistence.entity.VerificationZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VerificationZoneRepository extends JpaRepository<VerificationZone, UUID> {
}
