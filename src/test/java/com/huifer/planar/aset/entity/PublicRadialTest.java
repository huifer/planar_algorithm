package com.huifer.planar.aset.entity;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class PublicRadialTest {

    @Test
    public void te() {
        PublicRadial pr1 = new PublicRadial(new GeometryFactory().createPoint(new Coordinate(0, 0)),
                new GeometryFactory().createPoint(new Coordinate(0, 1)));
        PublicRadial pr2 = new PublicRadial(new GeometryFactory().createPoint(new Coordinate(0, 3)),
                new GeometryFactory().createPoint(new Coordinate(1, 2)));

        double a1 = pr1.getA();
        double b1 = pr1.getB();
        double c1 = pr1.getC();

        double a2 = pr2.getA();
        double b2 = pr2.getB();
        double c2 = pr2.getC();
        double x = (c1 - b1 * c1 + b1 * c2) / (a1 * b1 - a2 * b1 - a1);
        double y = -(a1 * (c1 - c1 * b1 + b1 * c2) / (a1 * b1 - a2 * b1 - a1) + c1) / b1;


        System.out.println(y > 0);

        System.out.println(x);
        System.out.println(y);

    }

}
