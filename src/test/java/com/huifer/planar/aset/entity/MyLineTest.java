package com.huifer.planar.aset.entity;


import com.huifer.planar.aset.algo.LineOverInterface;
import com.huifer.planar.aset.algo.impl.ovlayer.LineOverCore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class MyLineTest {


    @Test
    public void testIntersect() {
//        MyPoint p1 = new MyPoint(2.44, 1.81);
//        MyPoint p2 = new MyPoint(2.1,1.05);
//        MyPoint p3 = new MyPoint(-0.2,1.72);
//        MyPoint p4 = new MyPoint(2.54,0.73);
//
//        MyLine l1 = new MyLine(p1, p2);
//        MyLine l2 = new MyLine(p3, p4);
//
//        boolean intresect = MyLine.isIntresect(l1, l2);
//        System.out.println(intresect);
//        MyPoint intersect = MyLine.intersect(l1, l2);
//        System.out.println(intersect);
//        MyPoint myPoint = new MyPoint();
//        MyLine.getProjectivePoint(new MyPoint(0, 0), 0.5, new MyPoint(1, 1), myPoint);
//        System.out.println(myPoint);

//        MyLine l = new MyLine(new MyPoint(10, 0), new MyPoint(10, 10));
//        MyPoint p1 = new MyPoint(7, 7);
//        MyPoint p2 = new MyPoint(7, 4);
//        MyPoint p3 = new MyPoint(12, 7);
//        MyPoint p4 = new MyPoint(12, 4);
//        System.out.println(l);
//        MyPoint projectivePoint = l.getProjectivePoint(p1);
//        System.out.println(projectivePoint);
        //
        LineOverInterface over = new LineOverCore();

        MyLine l1 = new MyLine(new MyPoint(2, 0), new MyPoint(2, 3));
        MyLine l2 = new MyLine(new MyPoint(0, 2), new MyPoint(3, 2));
        MyPoint intersect = over.intersectPoint(l1, l2);
//        System.out.println(intersect);
        log.info("{}", intersect);
    }


}
