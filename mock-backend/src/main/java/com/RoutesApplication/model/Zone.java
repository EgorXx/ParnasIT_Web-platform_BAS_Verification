package com.RoutesApplication.model;

import lombok.Data;

import java.util.Map;

@Data
public class Zone {

    private Long id;

    private String name;

    /**
     * GeoJSON Polygon
     * пример:
     * {
     *   "type": "Polygon",
     *   "coordinates": [[[lng,lat],...]]
     * }
     */
    private Map<String, Object> geometry;

}