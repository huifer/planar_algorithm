package com.huifer.planar.aset;

import java.util.HashSet;
import java.util.Set;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

/**
 * <p>Title : OT </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-03-08
 */
public class OT {

    public static void main(String[] args) throws Exception {

        Geometry p5 = new WKTReader()
                .read("POLYGON ((88066.09 73721.882, 88065.09 73721.882, 88065.09 73721.882, 88065.09 73720.882, 88065.09 73720.882, 88065.09 73720.788, 88065.09 73720.788, 88065.281 73720.942, 88065.281 73720.942, 88066.284 73721.75, 88066.284 73721.75, 88066.09 73721.882))");

        Set<Point> ps = new HashSet<>();
        for (int i = 0; i < p5.getCoordinates().length; i++) {
            ps.add(new GeometryFactory().createPoint(p5.getCoordinates()[i]));
        }

        Geometry geometry = p5.convexHull();
        System.out.println(p5.getArea());
        System.out.println(geometry.getArea());

        System.out.println(ps.size());
        System.out.println(geometry.getCoordinates().length);

        System.out.println(geometry);


    }


}
