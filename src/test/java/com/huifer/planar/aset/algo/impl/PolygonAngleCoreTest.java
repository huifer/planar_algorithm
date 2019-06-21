package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.PolygonAngleInterface;
import com.huifer.planar.aset.algo.impl.polygon.PolygonAngleCore;
import com.huifer.planar.aset.entity.PolygonAngleResult;
import org.junit.Test;

public class PolygonAngleCoreTest {
    @Test
    public void testPolygonWktAngleMerge() throws Exception {
        String wkt = "POLYGON ((290.890041493776 392.188796680498,279.707468879668 191.919087136929,307.155601659751 327.126556016598,363.06846473029 170.570539419087,548.089211618257 237.665975103734,605.01867219917 407.4377593361,493.192946058091 301.711618257261,472.860995850622 232.582987551867,479.977178423236 311.877593360996,290.890041493776 392.188796680498))";
        PolygonAngleInterface polygonAngleInterface = new PolygonAngleCore();
        PolygonAngleResult polygonAngleResult = polygonAngleInterface.polygonWktAngleMerge(wkt, 30.0);
        System.out.println(polygonAngleResult.getNewPolygon());
    }

}
