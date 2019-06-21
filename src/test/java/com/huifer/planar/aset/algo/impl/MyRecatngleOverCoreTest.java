package com.huifer.planar.aset.algo.impl;


import com.huifer.planar.aset.algo.MyRectangleOver;
import com.huifer.planar.aset.algo.impl.polygon.MyRecatngleOverCore;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.MyRectangle;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class MyRecatngleOverCoreTest {

    MyRectangleOver over = new MyRecatngleOverCore();


    @Test
    public void testCalcAreaIntersectionRectangles() {
        MyRectangle r1 = new MyRectangle(new MyPoint(0, 0), 3, 2);
        MyRectangle r2 = new MyRectangle(new MyPoint(2, 0), 2, 3);
        double v = over.calcAreaIntersectionRectangles(r1, r2);
//        System.out.println(v);
        log.info("{}", v);
        assert 2 == v;
        r2 = new MyRectangle(new MyPoint(3, 0), 3, 2);
        double v1 = over.calcAreaIntersectionRectangles(r1, r2);
//        System.out.println(v1);
        log.info("{}", v1);
        assert 0 == v1;
        r2 = new MyRectangle(new MyPoint(10, 0), 1, 3);
        double v2 = over.calcAreaIntersectionRectangles(r1, r2);
//        System.out.println(v2);
        log.info("{}", v2);
        assert 0 == v2;

    }

}
