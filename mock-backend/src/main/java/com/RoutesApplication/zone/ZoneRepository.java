package com.RoutesApplication.zone;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ZoneRepository {

    private final List<Zone> zones = new ArrayList<>();

    private long sequence = 1;


    public Zone save(Zone zone) {

        if (zone.getId() == null) {
            zone.setId(sequence++);
            zones.add(zone);
        }

        return zone;
    }


    public List<Zone> findAll() {

        return zones;

    }


    public Optional<Zone> findById(Long id) {

        return zones.stream()
                .filter(zone -> zone.getId().equals(id))
                .findFirst();

    }


    public boolean deleteById(Long id) {

        return zones.removeIf(
                zone -> zone.getId().equals(id)
        );

    }

}