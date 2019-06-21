package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.algo.impl.polygon.KmeanPolygonSplitCore;
import org.junit.Test;

public class KmeanPolygonSplitInterfaceTest {

    @Test
    public void splitPolygonWithShpTest() throws Exception {

        KmeanPolygonSplitInterface km = new KmeanPolygonSplitCore();
        Object split = km.splitPolygonWithShp("E:\\mck\\planar_algorithm\\src\\main\\resources\\data\\shp\\555555.shp", "split");
    }

}
