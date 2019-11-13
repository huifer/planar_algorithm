package com.huifer.planar.aset.algo.impl.minrect;

import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class MinimumBoundingRectangleTest {

    @Test
    public void test() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        String wkt = "POLYGON ((87623.0828822501 73753.4143904365,87620.1073981973 73739.213216548,87629.1690996309 73730.4220136646,87641.882531493 73727.3112803367,87643.0997749692 73714.8683470248,87662.0346734872 73725.0120426595,87669.0676357939 73735.1557382941,87655.9484561064 73735.9672339449,87676.9120937514 73747.4634223308,87651.8909778525 73740.8362078495,87659.4649372597 73755.4431295634,87644.4522677204 73748.680665807,87645.5342619215 73760.7178512935,87635.2553170117 73750.9799034842,87630.5215923822 73760.3121034681,87623.0828822501 73753.4143904365))";
        Polygon read = (Polygon) new WKTReader().read(wkt);
        Polygon polygon = MinimumBoundingRectangle.get(read, gf);

        System.out.println(polygon);
        System.out.println(polygon.getArea());

    }
}
