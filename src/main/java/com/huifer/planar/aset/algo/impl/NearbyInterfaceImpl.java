package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.NearbyInterface;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author: huifer
 * @date: 2019-11-13
 */
@Slf4j
public class NearbyInterfaceImpl implements NearbyInterface {
    private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    /**
     * 点附近的面
     *
     * @param point       点
     * @param polygonList 面集合
     * @return 最近的面
     */
    @Override
    public Polygon pointNearbyPolygon(Point point, List<Polygon> polygonList) {

        List<PolygonNear> polygonNears = new ArrayList<>();
        for (Polygon polygon : polygonList) {
            PolygonNear polygonNear = new PolygonNear();
            polygonNear.setPolygon(polygon);
            List<Point> points = polygon2point(polygon);
            polygonNear.setPoints(points);
            Point point1 = pointNearbyPoint(point, points);
            polygonNear.setLength(point1.getLength());
            polygonNears.add(polygonNear);
        }

        List<PolygonNear> collect = polygonNears.stream()
                .sorted(Comparator.comparingDouble(PolygonNear::getLength))
                .collect(Collectors.toList());
        return collect.get(0).getPolygon();
    }

    private List<Point> polygon2point(Polygon pg) {
        List<Point> res = new ArrayList<>();
        for (Coordinate coordinate : pg.getCoordinates()) {
            Point p2 = new GeometryFactory().createPoint(coordinate);
            res.add(p2);
        }
        return res;
    }

    /**
     * 点附近的点, 暴力解法 , 提升的一种方式: 分治法
     *
     * @param point  点
     * @param points 点集合
     * @return 最近的点
     */
    @Override
    public Point pointNearbyPoint(Point point, List<Point> points) {
        List<PointNear> pointNears = new ArrayList<>();
        for (Point point1 : points) {
            Coordinate[] coordinates = {point.getCoordinate(), point1.getCoordinate()};
            LineString lineString = geometryFactory.createLineString(coordinates);
            PointNear build = PointNear.builder().length(lineString.getLength()).point(point1).build();
            pointNears.add(build);
        }
        log.info("距离集合={}", pointNears);
        List<PointNear> collect = pointNears.stream()
                .sorted(Comparator.comparingDouble(PointNear::getLength))
                .collect(Collectors.toList());

        return collect.get(0).getPoint();
    }


    /**
     * 点附近最近的点
     *
     * @param point       待测点
     * @param points      点集合
     * @param polygonList 作用范围
     * @return 最近的点
     */
    @Override
    public Point pointNearByPointAndPolygon(Point point, List<Point> points, List<Polygon> polygonList) {
        // 1. point 找到覆盖的面
        Polygon polygon = calcPointInPolygon(point, polygonList);
        List<Point> points1 = calcPointsInPolygon(points, polygon);
        return this.pointNearbyPoint(point, points1);
    }

    private List<Point> calcPointsInPolygon(List<Point> points, Polygon polygon) {
        List<Point> ps = new ArrayList<>();
        for (Point point : points) {
            if (polygon.intersects(point) || polygon.contains(point)) {
                ps.add(point);
            }
        }
        return ps;
    }

    private Polygon calcPointInPolygon(Point point, List<Polygon> polygonList) {
        for (Polygon polygon : polygonList) {
            if (polygon.intersects(point) || polygon.contains(point)) {
                return polygon;
            }
        }
        return null;
    }


    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class PolygonNear {
        private Polygon polygon;
        private List<Point> points;
        private Double length;
    }

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class PointNear {
        private Point point;
        private Double length;
    }


}
