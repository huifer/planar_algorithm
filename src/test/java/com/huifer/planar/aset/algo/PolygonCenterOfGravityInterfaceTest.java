package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.algo.impl.polygon.PolygonCenterOfGravityCore;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class PolygonCenterOfGravityInterfaceTest {

    private Polygon pg;
    private PolygonCenterOfGravityInterface polygonCenterOfGravityInterface = new PolygonCenterOfGravityCore();
    @Before
    public void init()throws Exception{
        String pgwkt = "POLYGON ((87969.0159375285 73820.8504159828,87945.8039237902 73809.5760093099,87946.1355239865 73792.0011989081,87953.099128108 73779.4003914502,87969.3475377248 73775.7527892913,87995.5439532294 73778.4055908614,88006.8183599022 73790.3431979268,88006.1551595097 73802.9440053847,88002.5075573508 73814.8816124501,87992.2279512667 73823.5032175529,87980.6219443976 73823.5032175529,87969.0159375285 73820.8504159828))";
//        pgwkt = "POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35 35, 30 20, 20 30))";
        Polygon read = (Polygon) new WKTReader().read(pgwkt);
        pg = read;

//        DelaunayTriangulationBuilder a = new DelaunayTriangulationBuilder();
//        a.setSites(read);
//        Geometry triangles = a.getTriangles(new GeometryFactory());
//
//        int numGeometries = triangles.getNumGeometries();
//        for (int i = 0; i < numGeometries; i++) {
//            Geometry geometryN = triangles.getGeometryN(i);
//            System.out.println(geometryN);
//        }
//
//        System.out.println();


    }

    @Test
    public void testClcPolygonGravitePoint() throws Exception {

        Point point = polygonCenterOfGravityInterface.calcGravityPointOfCovenPolygon(pg);
        System.out.println(point);
    }
}
