package com.RoutesApplication.zone;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class Zone {

    private Long id;

    private String name;


    /**
     * Геометрия зоны.
     *
     * Формат:
     *
     * [
     *   {
     *     "lat_0": 55.7558,
     *     "lng_0": 37.6173
     *   },
     *   {
     *     "lat_1": 56.0,
     *     "lng_1": 38.0
     *   }
     * ]
     *
     * Последняя точка должна совпадать с первой.
     */
    private List<Map<String, Double>> geometry;

}