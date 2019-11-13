package com.huifer.planar.aset.algo;


import com.huifer.planar.aset.algo.impl.polygon.RailCore;
import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.MyPolygon;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;

@Slf4j
public class RailCoreTest {

    @Test
    public void Test() {
        MyPoint testP = new MyPoint(0.5, 0.5);

        MyPoint p1 = new MyPoint(0, 0);
        MyPoint p2 = new MyPoint(1, 0);
        MyPoint p3 = new MyPoint(1, 1);
        MyPoint p4 = new MyPoint(0, 1);

        MyLine l1 = new MyLine(p1, p2);
        MyLine l2 = new MyLine(p2, p3);
        MyLine l3 = new MyLine(p3, p4);

        ArrayList<MyLine> lines = new ArrayList<>();
        lines.add(l1);
        lines.add(l2);
        lines.add(l3);

        MyPolygon pg = new MyPolygon(lines);
        RailInterface railInterface = new RailCore();
        boolean inPolygon = railInterface.isInPolygon(testP, pg);
//        System.out.println(inPolygon ? "IN" : "OUT");
        log.info("是否在面内 = {}", inPolygon ? "IN" : "OUT");
    }
}
