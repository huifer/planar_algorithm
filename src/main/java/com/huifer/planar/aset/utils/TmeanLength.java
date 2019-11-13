package com.huifer.planar.aset.utils;

import com.huifer.planar.aset.entity.FourBox;
import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Title : TmeanLength </p>
 * <p>Description : 平面平均宽度</p>
 *
 * @author huifer
 * @date 2019-01-11
 */
public class TmeanLength {

    private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    public static void main(String[] args) throws Exception {
        // 已知一个平面坐标求平均宽度
        Operation op = new Operation();
        TmeanLength tmeanLength = new TmeanLength();
        String polygon = "POLYGON((0 0, 10 20, 20 20, 10 0, 0 0))";
        Polygon polygonByWKT = op.createPolygonByWKT(polygon);
        // 1. 构建一条垂直线(y无限大无限小) 得L1
        // 1.1 求面的四至
        Coordinate[] coordinates = polygonByWKT.getCoordinates();
        List<Coordinate> collect = Arrays.stream(coordinates).collect(Collectors.toList());
        FourBox bbox = calcSpaceBetween(collect);
        // 垂直 、
        double verticalDistance = bbox.getRightY() - bbox.getLeftY();
        // 水平距离
        double horizontalDistance = bbox.getRightX() - bbox.getLeftX();
        // 平均切割10分
        int step = 10;
        ArrayList<Double> length = new ArrayList<>();
        for (int i = 0; i <= step; i++) {
            double spaceLength = horizontalDistance / step;
            double nowX = bbox.getLeftX() + i * spaceLength;
            // 2. L1与平面坐标相交 根据间隔 interval 扫描整个平面 直线L1 与平面的交点 构成新的直线 courseLine 求直线长度
            LineString ccc = tmeanLength.createVerticalLine(nowX, bbox);
            Geometry intersection = polygonByWKT.intersection(ccc);
            length.add(intersection.getLength());
        }
        // 3. 把所有 courseLine 取平均值
        System.out.println(length);
    }

    /**
     * 求这个面的四至
     */
    public static FourBox calcSpaceBetween(List<Coordinate> collect) {
        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();
        collect.forEach(
                s -> {
                    xList.add(s.x);
                    yList.add(s.y);
                }
        );

        FourBox fourBox = new FourBox();
        fourBox.setLeftX(Collections.min(xList));
        fourBox.setLeftY(Collections.min(yList));
        fourBox.setRightX(Collections.max(xList));
        fourBox.setRightY(Collections.max(yList));
        return fourBox;
    }


    /**
     * 获取面的所有线段
     */
    public List<LineString> getPolygonLine(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();
        List<LineString> res = new ArrayList<>();
        for (int i = 0; i < coordinates.length - 1; i++) {
            if (i + 1 == coordinates.length - 1) {
                Coordinate[] coords = new Coordinate[]{coordinates[i], coordinates[0]};
                LineString line = geometryFactory.createLineString(coords);
                res.add(line);
            } else {
                Coordinate[] coords = new Coordinate[]{coordinates[i], coordinates[i + 1]};
                LineString line = geometryFactory.createLineString(coords);
                res.add(line);
            }
        }
        return res;
    }

    /***
     * 构造垂线
     * @param x x坐标值
     * @return 直线
     */
    public LineString createVerticalLine(double x, FourBox box) {
        Coordinate[] coords = new Coordinate[]{new Coordinate(x, box.getLeftY() - 1),
                new Coordinate(x, box.getRightY() + 1)};
        LineString line = geometryFactory.createLineString(coords);
        return line;
    }


    /***
     * 构造水平线
     * @param y y坐标值
     * @return 直线
     */
    public LineString createHorizontalLine(double y, FourBox box) {
        Coordinate[] coords = new Coordinate[]{new Coordinate(box.getLeftX() - 1, y),
                new Coordinate(box.getRightX() + 1, y)};
        LineString line = geometryFactory.createLineString(coords);
        return line;
    }

}
