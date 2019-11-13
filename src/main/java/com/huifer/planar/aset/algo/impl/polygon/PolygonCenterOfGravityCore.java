package com.huifer.planar.aset.algo.impl.polygon;

import com.huifer.planar.aset.algo.PolygonCenterOfGravityInterface;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Title : PolygonCenterOfGravityCore </p>
 * <p>Description : 多边形重心实现</p>
 *
 * @author huifer
 * @date 2019-02-21
 */
public class PolygonCenterOfGravityCore implements PolygonCenterOfGravityInterface {

    @Override
    public Point calcGravityPointOfConcavePolygon(Polygon concavePolygon) {

        return null;
    }

    @Override
    public Point calcGravityPointOfCovenPolygon(Polygon covenPolygon) {
//        System.out.println("=========================");
//        System.out.println(covenPolygon.getArea());
//        System.out.println("=========================");
        List<Polygon> delaunaryList = doDelaunayTriangle(covenPolygon);

        double xArea = 0.0;
        double yArea = 0.0;
        double aArea = 0.0;
        for (int i = 0; i < delaunaryList.size(); i++) {
            Polygon polygon = delaunaryList.get(i);
            if (polygon.within(covenPolygon)) {
//                System.out.println(polygon);
                double oneArea = polygon.getArea();
                Point oneGravity = triangleCenterOfGravity(polygon);
                xArea += oneGravity.getX() * oneArea;
                yArea += oneGravity.getY() * oneArea;
                aArea += oneArea;
            }
        }

//        System.out.println(aArea);
        return new GeometryFactory().createPoint(new Coordinate((xArea / aArea), (yArea / aArea)));
    }

    /**
     * 三角形重心计算
     *
     * @param triangle 三角形
     * @return 重心
     */
    private Point triangleCenterOfGravity(Polygon triangle) {

        // 获取三角形角点
        Set<Coordinate> vertexSet = Arrays.stream(triangle.getCoordinates())
                .collect(Collectors.toSet());
        double[] result = new double[2];
        Point rPoint = null;
        if (vertexSet.size() == 3) {
            List<Coordinate> collect = vertexSet.stream().collect(Collectors.toList());
            double gX = (collect.get(0).x + collect.get(1).x + collect.get(2).x) / 3;
            double gY = (collect.get(0).y + collect.get(1).y + collect.get(2).y) / 3;
            rPoint = new GeometryFactory().createPoint(new Coordinate(gX, gY));
            result = new double[]{gX, gY};
        }
        return rPoint;

    }

    /**
     * 计算德劳内三角形切分结果
     *
     * @param pg 输入面
     * @return 德劳内三角形结果集合
     */
    private List<Polygon> doDelaunayTriangle(Polygon pg) {
        VoronoiInterfaceImpl vor = new VoronoiInterfaceImpl();
        List<Geometry> delaunay = vor
                .delaunay(Arrays.stream(pg.getCoordinates()).collect(Collectors.toList()));
        List<Polygon> polygons = geom2Pg(delaunay);
        return polygons;
    }

    /**
     * 德劳内geom转换 Polygon
     *
     * @param geoms 德劳内geom
     * @return 德劳内三角形结果集合
     */
    private List<Polygon> geom2Pg(List<Geometry> geoms) {
        List<Polygon> pg = new ArrayList<>();
        geoms.forEach(
                s -> {
                    pg.add(new GeometryFactory().createPolygon(s.getCoordinates()));
                }
        );
        return pg;
    }
}
