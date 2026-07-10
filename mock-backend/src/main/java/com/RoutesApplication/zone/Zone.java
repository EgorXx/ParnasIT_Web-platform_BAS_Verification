package com.RoutesApplication.zone;

import com.RoutesApplication.module.Point;
import lombok.Data;

import java.util.List;


@Data
public class Zone {

    private Long id;

    private String name;

    private List<Point> coordinates;

}