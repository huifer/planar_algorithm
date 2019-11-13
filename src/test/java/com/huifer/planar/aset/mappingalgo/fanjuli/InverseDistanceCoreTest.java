package com.huifer.planar.aset.mappingalgo.fanjuli;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * idw
 */
public class InverseDistanceCoreTest {

    @Test
    public void testIdw() {
        ReadHelper readHelper = new ReadHelper(
                "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\mappingalgo\\fanjuli\\测站坐标.txt");
        List<MyPoint> points = readHelper.getPoints();

        List<MyPoint> paramList = new ArrayList<>();
        MyPoint p = new MyPoint("Q1", 4310, 3600, 0);
        HashMap<String, Object> idw = InverseDistanceCore.idw(points, p, 5);
        System.out.println(idw);

    }
}
