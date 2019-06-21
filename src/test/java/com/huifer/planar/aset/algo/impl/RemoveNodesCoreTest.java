package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.impl.line.RemoveNodesCore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

@Slf4j
public class RemoveNodesCoreTest {

    private Point point;
    private LineString linestring;
    private LinearRing linearring;
    private Polygon polygon1;
    private Polygon polygon2;
    private MultiPolygon multipolygon;
    private GeometryCollection geometrycollection;


    @Before
    public void init() throws Exception {

        point = (Point) new WKTReader().read("POINT (30 10)");
        linestring = (LineString) new WKTReader().read("LINESTRING (30 10, 10 30, 40 40)");
        polygon1 = (Polygon) new WKTReader().read("POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))");
        polygon2 = (Polygon) new WKTReader()
                .read("POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10),(20 30, 35 35, 30 20, 20 30))");
        multipolygon = (MultiPolygon) new WKTReader()
                .read("MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)),((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),(30 20, 20 15, 20 25, 30 20)))");

        linearring = (LinearRing) polygon1.getExteriorRing();
        Collection<Geometry> oc = new ArrayList<>();
        oc.add(polygon1);
        oc.add(polygon2);
        oc.add(multipolygon);
        geometrycollection = (GeometryCollection) new GeometryFactory().buildGeometry(oc);

    }

    @Test
    public void t() {

        RemoveNodesCore removeNodesCore = new RemoveNodesCore();

        Geometry remove = removeNodesCore.remove(point, new Coordinate(30, 10));
        Geometry remove1 = removeNodesCore.remove(linestring, new Coordinate(30, 10));
        Geometry remove6 = removeNodesCore.remove(linearring, new Coordinate(30, 10));
        Geometry remove2 = removeNodesCore.remove(polygon1, new Coordinate(30, 10));
        Geometry remove3 = removeNodesCore.remove(polygon2, new Coordinate(30, 10));
        Geometry remove4 = removeNodesCore.remove(multipolygon, new Coordinate(30, 10));
        Geometry remove5 = removeNodesCore.remove(multipolygon, new Coordinate(10, 30));
        Geometry remove7 = removeNodesCore.remove(geometrycollection, new Coordinate(30, 10));

        List<Coordinate> cs = new ArrayList<>();
        cs.add(new Coordinate(30, 10));
        cs.add(new Coordinate(20, 35));
        removeNodesCore.removeList(geometrycollection, cs);

        log.info("{}", point);
        log.info("{}", remove);

        log.info("{}", linestring);
        log.info("{}", remove1);

        log.info("{}", linearring);
        log.info("{}", remove6);

        log.info("{}", polygon1);
        log.info("{}", remove2);

        log.info("{}", polygon2);
        log.info("{}", remove3);

        log.info("{}", multipolygon);
        log.info("{}", remove4);

        log.info("{}", multipolygon);
        log.info("{}", remove5);

        log.info("{}", geometrycollection);
        log.info("{}", remove7);

        System.out.println();
    }


}
