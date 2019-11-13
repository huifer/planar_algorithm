package com.huifer.planar.aset.algo.impl;


import com.huifer.planar.aset.algo.NearbyInterface;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NearbyInterfaceImplTest {
    NearbyInterface nearbyInterface = new NearbyInterfaceImpl();
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    @Test
    public void pointNearbyPolygon() {
    }

    @Test
    public void pointNearbyPoint() {

        Coordinate coordinate = new Coordinate(1, 2);

        Point p = new GeometryFactory().createPoint(coordinate);
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            Coordinate coordinate2 = new Coordinate(random.nextDouble(), random.nextDouble());

            Point p2 = new GeometryFactory().createPoint(coordinate2);
            list.add(p2);

        }
        Point point = nearbyInterface.pointNearbyPoint(p, list);
        System.out.println("结果为" + point);
    }
}
