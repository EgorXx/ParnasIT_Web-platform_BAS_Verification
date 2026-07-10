package com.RoutesApplication.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Route {

    private Long id;

    private String name;

    private List<Point> points;

}