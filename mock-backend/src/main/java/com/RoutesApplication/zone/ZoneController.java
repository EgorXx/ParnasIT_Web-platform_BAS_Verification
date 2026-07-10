package com.RoutesApplication.zone;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {


    private final ZoneRepository repository;


    /**
     * POST /api/zones
     * Создание зоны
     * Ответ: 201 Created
     */
    @PostMapping
    public ResponseEntity<Zone> create(
            @RequestBody Zone zone
    ) {

        Zone saved = repository.save(zone);

        return ResponseEntity
                .created(
                        URI.create("/api/zones/" + saved.getId())
                )
                .body(saved);
    }


    /**
     * GET /api/zones
     * Получить все зоны
     */
    @GetMapping
    public List<Zone> getAll() {

        return repository.findAll();

    }


    /**

     * GET /api/zones/{id}

     * Получить зону по id

     */

    @GetMapping("/{id}")

    public ResponseEntity<Zone> getById(

            @PathVariable Long id

    ) {

        return repository.findById(id)

                .map(ResponseEntity::ok)

                .orElseGet(() -> ResponseEntity.notFound().build());

    }

}