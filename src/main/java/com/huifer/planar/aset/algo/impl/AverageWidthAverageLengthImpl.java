package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.AverageWidthAverageLength;
import com.huifer.planar.aset.entity.FourBox;
import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

/**
 * <p>Title : AverageWidthAverageLengthImpl </p>
 * <p>Description : AverageWidthAverageLength 实现</p>
 *
 * @author huifer
 * @date 2019-01-14
 */
@Slf4j
public class AverageWidthAverageLengthImpl implements AverageWidthAverageLength {


    /***
     * JTS工厂
     */
    private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    private Operation op = new Operation();


    /**
     * 求四至
     *
     * @param collect 点集
     * @return FourBox
     */
    private FourBox calcSpaceBetween(List<Coordinate> collect) {
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

    @Override
    public double calcAverageWidth(int count, String wkt) {
        try {
            Polygon polygonByWKT = op.createPolygonByWKT(wkt);
            Coordinate[] coordinates = polygonByWKT.getCoordinates();
            List<Coordinate> collect = Arrays.stream(coordinates).collect(Collectors.toList());
            FourBox bbox = calcSpaceBetween(collect);
            double horizontalDistance = bbox.getRightX() - bbox.getLeftX();

            ArrayList<Double> length = new ArrayList<>();
            for (int i = 0; i <= count; i++) {
                double spaceLength = horizontalDistance / count;
                double nowX = bbox.getLeftX() + i * spaceLength;
                LineString ccc = createVerticalLine(nowX, bbox);
                Geometry intersection = polygonByWKT.intersection(ccc);
                length.add(intersection.getLength());
            }
            log.info("{}" + length);
            double asDouble = length.stream().mapToDouble(Double::doubleValue).average()
                    .getAsDouble();
            return asDouble;

        } catch (ParseException e) {
            e.printStackTrace();
            log.error("{}" + e);

        }

        return 0;
    }

    @Override
    public double calcAverageLength(int count, String wkt) {

        try {
            Polygon polygonByWKT = op.createPolygonByWKT(wkt);
            Coordinate[] coordinates = polygonByWKT.getCoordinates();
            List<Coordinate> collect = Arrays.stream(coordinates).collect(Collectors.toList());
            FourBox bbox = calcSpaceBetween(collect);
            double verticalDistance = bbox.getRightY() - bbox.getLeftY();

            ArrayList<Double> length = new ArrayList<>();
            for (int i = 0; i <= count; i++) {
                double spaceLength = verticalDistance / count;
                double nowX = bbox.getLeftX() + i * spaceLength;
                LineString ccc = createHorizontalLine(nowX, bbox);
                Geometry intersection = polygonByWKT.intersection(ccc);
                length.add(intersection.getLength());
            }
            log.info("长度 {}",length);
            double asDouble = length.stream().mapToDouble(Double::doubleValue).average()
                    .getAsDouble();
            return asDouble;
        } catch (ParseException e) {
            log.error("{}", e);
            e.printStackTrace();
        }

        return 0;
    }
}
