package com.RoutesApplication.controller;

import com.RoutesApplication.model.Zone;
import com.RoutesApplication.repository.ZoneRepository;

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
     * PUT /api/zones/{id}
     * Обновление зоны
     */
    @PutMapping("/{id}")
    public ResponseEntity<Zone> update(
            @PathVariable Long id,
            @RequestBody Zone updatedZone
    ) {

        return repository.findById(id)
                .map(existing -> {

                    existing.setName(updatedZone.getName());
                    existing.setGeometry(updatedZone.getGeometry());

                    return ResponseEntity.ok(existing);

                })
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );

    }


    /**
     * DELETE /api/zones/{id}
     * Удаление зоны
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        boolean deleted = repository.deleteById(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }

}