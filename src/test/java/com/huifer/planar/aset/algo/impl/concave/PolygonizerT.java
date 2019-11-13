package com.huifer.planar.aset.algo.impl.concave;

import com.huifer.planar.aset.utils.CommonUtils;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

import java.util.List;

/**
 * <p>Title : PolygonizerT </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-02-28
 */
public class PolygonizerT {

    @Test
    public void test() throws Exception {
        Geometry rs = new WKTReader()
                .read("POLYGON ((88075.4230267276 73722.549590726,88076.0624386539 73723.4898983432,88076.0624386539 73724.4898983432,88075.0624386539 73724.4898983432,88075.9684063663 73725.7466350986,88074.1065975894 73725.5585743381,88075.1221310367 73724.2985607578,88073.8997303714 73722.6436191989,88074.8997303714 73722.6436191989,88073.8056980838 73721.4212185336,88076.3069187869 73721.3836056186,88075.4230267276 73722.549590726))");
        Geometry ls = new WKTReader()
                .read("LINESTRING (88073.8056980838 73725.93191244664, 88076.3069187869 73722.82854636687)");
        List<LineString> lineStrings = CommonUtils.polygonLineString((Polygon) rs);

        Geometry read = new WKTReader()
                .read("POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2))");

        Boolean clipPolygon = CommonUtils.isClipPolygon((Polygon) read);
        Boolean c2 = CommonUtils.isClipPolygon((Polygon) rs);
        System.out.println();

        String splitPoint = "POINT (88075.1221310367 73724.2985607578)";
        LineString lsssss = (LineString) ls;
        Geometry read1 = new WKTReader().read(splitPoint);
        Point sp = (Point) read1;

        List<LineString> lineStrings1 = CommonUtils.lineStringSplit(lsssss, sp);
        lineStrings1.forEach(s -> System.out.println(s));
        System.out.println();
    }

}
