package com.huifer.planar.aset.algo.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class PolygonSegmentationCoreTest {

    @Test
    public void testSortByPoint() {
        List<Point> ps = new ArrayList<>();
        ps.add(new GeometryFactory().createPoint(new Coordinate(0, 0)));
        ps.add(new GeometryFactory().createPoint(new Coordinate(1, 1)));
        ps.add(new GeometryFactory().createPoint(new Coordinate(3, 3)));
        ps.add(new GeometryFactory().createPoint(new Coordinate(2, 2)));
        Point point = new GeometryFactory().createPoint(new Coordinate(3, 3));

        List<Point> left = new ArrayList<>();
        List<Point> right = new ArrayList<>();
        List<Point> result = new ArrayList<>();
        int i1 = ps.indexOf(point);

        for (int i = 0; i < ps.size(); i++) {
            if (i < i1) {
                right.add(ps.get(i));
            }
            if (i == i1) {
                result.add(ps.get(i));
            }
            if (i > i1) {
                left.add(ps.get(i));
            }
        }
        result.addAll(left);
        result.addAll(right);

        System.out.println();


    }

}
