package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.impl.line.RemoveLine;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class RemoveLineTest {
    @Test
    public void tt() throws Exception {
        Polygon polygon1 = (Polygon) new WKTReader()
                .read("POLYGON ((88079.50995826721 73725.33817863464, 88079.50995826721 73723.938539505, 88076.85249519348 73722.38814735413, 88079.65061759949 73720.67353248596, 88079.50129890442 73718.87973594666, 88078.85879325867 73717.1983127594, 88080.6580028534 73717.14500236511, 88081.77146720886 73714.7630367279, 88078.27829933167 73712.19784355164, 88073.20011329651 73715.0487537384, 88068.65647697449 73720.30512428284, 88079.50995826721 73725.33817863464))");

        Geometry geometry = RemoveLine.delete(polygon1, 2);
//        System.out.println(geometry);

    }

}
