package com.huifer.planar.aset.utils;

import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class PolygonUtilsTest {

    @Test
    public void testSplit() throws Exception {
        String wkt = "POLYGON ((88064.3489745457 73720.9215322899,88064.123424185 73716.9969560137,88068.4765461465 73716.0947545709,88068.950201904 73719.1396844404,88066.8074734774 73717.4931668073,88067.348794343 73720.312546316,88065.7022767099 73718.4855883943,88066.063157287 73721.7109585523,88064.9579605196 73719.4780099814,88064.3489745457 73720.9215322899))";
        Geometry rs = new WKTReader().read(wkt);
        Polygon polygon1 = new GeometryFactory().createPolygon(rs.getCoordinates());
        String li = "LINESTRING (88064.98762193692 73716.0947545709, 88072.29638054271 73721.7109585523)";
        Geometry ls = new WKTReader().read(wkt);
        LineString lineString = new GeometryFactory().createLineString(ls.getCoordinates());
        Geometry geometry = PolygonUtils.splitPolygon(polygon1, lineString);
        int numGeometries = geometry.getNumGeometries();
        for (int i = 0; i < numGeometries; i++) {
            Geometry geometryN = geometry.getGeometryN(i);
            System.out.println(geometryN);
        }

    }

}
