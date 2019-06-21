package com.huifer.planar.aset.algo.impl.minrect;

import com.huifer.planar.aset.algo.MinimumEnclosingInterface;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class MinimumEnclosingCoreTest {

    private Geometry read;
    private MinimumEnclosingInterface m = new MinimumEnclosingCore();

    @Before
    public void init() throws Exception {
        read = new WKTReader()
                .read("POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10))");
        System.out.println("原图");
        System.out.println(read);
    }


    @Test
    public void getMinimumCircumscribedCircle() throws Exception {

        Polygon minimumCircumscribedCircle = m.getMinimumCircumscribedCircle((Polygon) read);
        System.out.println("最小外接圆");
        System.out.println(minimumCircumscribedCircle);
    }

    @Test
    public void getMinRect() {
        Polygon minRect = m.getMinRect(read);
        System.out.println("最小矩形");
        System.out.println(minRect);
    }


    @Test
    public void getInCirclePolygon() {
        Polygon inCirclePolygon = m.getInCirclePolygon((Polygon) read);
        System.out.println("最大内接圆");
        System.out.println(inCirclePolygon);
    }
}
