package com.huifer.planar.aset.algo.impl.minrect;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class MaximumInnerCircleTest {


    private Geometry read;

    @Before
    public void init() throws Exception {
        read = new WKTReader()
                .read("POLYGON ((88066.09 73721.882, 88065.09 73721.882, 88065.09 73721.882, 88065.09 73720.882, 88065.09 73720.882, 88065.09 73720.788, 88065.09 73720.788, 88065.281 73720.942, 88065.281 73720.942, 88066.284 73721.75, 88066.284 73721.75, 88066.09 73721.882))");
    }

    @Test
    public void inCirclePolygon() {
        Polygon polygon = MaximumInnerCircle.inCirclePolygon((Polygon) read, 0.00001);
        System.out.println(polygon);
    }

    @Test
    public void inCirclePoint() {
        Point polylabel = MaximumInnerCircle.inCirclePoint((Polygon) read, 0.00001);
        System.out.println(polylabel);
    }
}
