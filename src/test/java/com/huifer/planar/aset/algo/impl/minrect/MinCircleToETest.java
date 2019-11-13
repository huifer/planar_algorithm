package com.huifer.planar.aset.algo.impl.minrect;

import com.huifer.planar.aset.algo.impl.minrect.MinCircleToE.P;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

public class MinCircleToETest {

    Coordinate p1;
    Coordinate p2;
    Coordinate p3;

    @Before
    public void init() {
        p1 = new Coordinate(1, 1);
        p2 = new Coordinate(0, 1);
        p3 = new Coordinate(2, 3);
    }

    @Test
    public void threePointCircle() {
        Polygon polygon = MinCircleToE.threePointCircle(p1, p2, p3);
        System.out.println(polygon);
    }


    @Test
    public void minCircle() {
        P p1 = new P(1, 1);
        P p2 = new P(0, 1);
        P p3 = new P(2, 3);
        P p4 = new P(1.3, 1.3);
        List<P> pList = new ArrayList<>();
        pList.add(p1);
        pList.add(p2);
        pList.add(p3);
        pList.add(p4);

    }
}
