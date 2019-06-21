package com.huifer.planar.aset.algo;


import com.huifer.planar.aset.algo.impl.line.SimpleDijkstra;
import com.huifer.planar.aset.entity.*;
import com.huifer.planar.aset.utils.DijkstraUtil;
import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SimpleDijkstraTest {

    @Test
    public void test() {
        MyPoint p0 = new MyPoint(0, 0);
        MyPoint p1 = new MyPoint(1, 1);
        MyPoint p2 = new MyPoint(3, 2);
        MyPoint p3 = new MyPoint(2, 4);
        MyPoint p4 = new MyPoint(3, 5);
        MyPoint p5 = new MyPoint(0, 3);
        MyPoint p6 = new MyPoint(2, 0);
        MyPoint p7 = new MyPoint(3, 1);
        MyPoint p8 = new MyPoint(3, 0);
        MyPoint p9 = new MyPoint(4, 1);
        MyPoint p10 = new MyPoint(4, 6);
        MyPoint p11 = new MyPoint(4, 4);

        MyLine l0 = new MyLine(p0, p6);
        MyLine l1 = new MyLine(p6, p8);
        MyLine l2 = new MyLine(p3, p4);
        MyLine l3 = new MyLine(p1, p6);
        MyLine l4 = new MyLine(p6, p2);
        MyLine l5 = new MyLine(p8, p7);
        MyLine l6 = new MyLine(p9, p8);
        MyLine l7 = new MyLine(p5, p0);
        MyLine l8 = new MyLine(p4, p11);
        MyLine l9 = new MyLine(p11, p10);
        MyLine l10 = new MyLine(p9, p11);

        ArrayList<MyPoint> pointList = new ArrayList<>();
        pointList.add(p0);
        pointList.add(p1);
        pointList.add(p2);
        pointList.add(p3);
        pointList.add(p4);
        pointList.add(p5);
        pointList.add(p6);
        pointList.add(p7);
        pointList.add(p8);
        pointList.add(p9);
        pointList.add(p10);
        pointList.add(p11);

        ArrayList<MyLine> lineList = new ArrayList<>();

        lineList.add(l0);
        lineList.add(l1);
        lineList.add(l2);
        lineList.add(l3);
        lineList.add(l4);
        lineList.add(l5);
        lineList.add(l6);
        lineList.add(l7);
        lineList.add(l8);
        lineList.add(l9);
        lineList.add(l10);

        ArrayList<ArrayList<Double>> distances = DijkstraUtil.getDistances(pointList, lineList);

        DijlstraInterface dijlstraInterface = new SimpleDijkstra();

        ResultShortestPath integers = dijlstraInterface.shortestPathByStartAndEnd(distances, 1, 5);
        log.info("距离 = {}", integers);
//        System.out.println(integers);
        ArrayList<ResultShortestPath> arrayLists = dijlstraInterface
                .shortestPathByStart(distances, 1);
//        System.out.println(arrayLists);
        log.info("result = {}", arrayLists);
    }

}
