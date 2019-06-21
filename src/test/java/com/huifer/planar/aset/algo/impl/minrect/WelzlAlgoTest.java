package com.huifer.planar.aset.algo.impl.minrect;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class WelzlAlgoTest {

    @Test
    public void testOc() {
        List<Point> pointSet = new ArrayList<>();
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(6.82, 8.23)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(7.37, 6.06)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(8.23, 9.51)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(7.61, 6.37)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(9.16, 8.91)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(9.35, 7.46)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(7.45, 8.27)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(5.43, 9.44)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(5.15, 9.83)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(8.15, 5.11)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(9.58, 8.48)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(8.6, 8.79)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(8.15, 9.09)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(6.66, 6.94)));
        pointSet.add(new GeometryFactory().createPoint(new Coordinate(5.49, 9.69)));

        MinCircle circle = WelzlAlgo.mec(pointSet, pointSet.size() - 1);
        Geometry buffer = circle.getCenter().buffer(circle.getR());
        System.out.println(buffer);
        pointSet.forEach(s -> System.out.println(s));

    }

}
