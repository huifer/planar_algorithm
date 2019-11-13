package com.huifer.planar.aset.algo;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

/**
 * 描述:
 * 邻近算法
 *
 * @author: huifer
 * @date: 2019-11-13
 */
public interface NearbyInterface {
    /**
     * 点附近的面
     *
     * @param point       点
     * @param polygonList 面集合
     * @return 最近的面
     */
    Polygon pointNearbyPolygon(Point point, List<Polygon> polygonList);

    /**
     * 点附近的点
     *
     * @param point  点
     * @param points 点集合
     * @return 最近的点
     */
    Point pointNearbyPoint(Point point, List<Point> points);

    /**
     * 点附近最近的点
     *
     * @param point       待测点
     * @param points      点集合
     * @param polygonList 作用范围
     * @return 最近的点
     */
    Point pointNearByPointAndPolygon(Point point, List<Point> points, List<Polygon> polygonList);

}
