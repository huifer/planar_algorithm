package com.huifer.planar.aset.algo;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : VoronoiInterface </p>
 * <p>Description : 泰森多边形相关</p>
 *
 * @author huifer
 * @date 2019-01-16
 */
public interface VoronoiInterface {

    /**
     * 泰森多边形
     *
     * @param coords 坐标值
     * @return 泰森多边形面集合
     */
    List<Geometry> voronoi(List<Coordinate> coords);

    /**
     * 泰森多边形
     *
     * @param doubles 二维坐标组
     * @return 泰森多边形面集合
     */
    List<Geometry> voronoi(double[][] doubles);

    /**
     * 泰森多边形
     *
     * @param points 点集合
     * @return 泰森多边形面集合
     */
    List<Geometry> voronoi(ArrayList<Point> points);

    /**
     * 德劳内三角形
     *
     * @param coords 坐标值
     * @return 德劳内三角形面集合
     */
    List<Geometry> delaunay(List<Coordinate> coords);

    /**
     * 德劳内三角形
     *
     * @param doubles 二维坐标组
     * @return 德劳内三角形面集合
     */
    List<Geometry> delaunay(double[][] doubles);

    /**
     * 德劳内三角形
     *
     * @param points 点集合
     * @return 德劳内三角形面集合
     */
    List<Geometry> delaunay(ArrayList<Point> points);

}
