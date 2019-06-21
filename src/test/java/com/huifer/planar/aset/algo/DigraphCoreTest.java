package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.algo.impl.ovlayer.GraphCore;
import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.OvlayerEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DigraphCoreTest {

    @Test
    public void Test() {
        GraphInterface graphInterface = new GraphCore();

        MyPoint p0 = new MyPoint(48, 20);
        MyPoint p1 = new MyPoint(48, 50);

        MyLine l1 = new MyLine(p0, p1);

        // 左
        MyPoint ptest_1 = new MyPoint(36.46, 30.46);

        // 右
        MyPoint ptest_2 = new MyPoint(56.5959, 27.3467);
        // 上
        MyPoint ptest_3 = new MyPoint(48, 23);
        // 延长线上
        MyPoint ptest_4 = new MyPoint(48, 100);

        log.info("{}", "=================分割线=================");
//        System.out.println(l1.pointWhereIsLine(ptest_1));
        log.info("{}", l1.pointWhereIsLine(ptest_1));
        log.info("{}", "=================分割线=================");

        OvlayerEnum i1 = graphInterface.rightHandRule(ptest_1, l1);
        OvlayerEnum i2 = graphInterface.rightHandRule(ptest_2, l1);
        OvlayerEnum i3 = graphInterface.rightHandRule(ptest_3, l1);
        OvlayerEnum i4 = graphInterface.rightHandRule(ptest_4, l1);
        log.info("{}", i1);
        log.info("{}", i2);
        log.info("{}", i3);
        log.info("{}", i4);
//        System.out.println(i1);
//        System.out.println(i2);
//        System.out.println(i3);
//        System.out.println(i4);
//
//        //////////////////////////////////////////////////////// 线段交点 /////////////////////////
//        MyPoint jp1 = new MyPoint(143.2840, 83.7583);
//        MyPoint jp2 = new MyPoint(102.9240, 28.9930);
//        MyPoint jp3 = new MyPoint(102.6513, 71.7698);
//        MyPoint jp4 = new MyPoint(138.3754, 33.8974);
//
//        MyLine jl1 = new MyLine(jp1, jp2);
//        MyLine jl2 = new MyLine(jp3, jp4);
//
//        MyPoint intersectionPoint = graphInterface.getIntersectionPoint(jl1, jl2);
//        System.out.println(intersectionPoint);
//
//        //////////////////////////  线段是否相交 //////////////////
//        MyPoint lp1 = new MyPoint(210.5281, 91.3681);
//        MyPoint lp2 = new MyPoint(180.3922, 38.5229);
//        MyPoint lp3 = new MyPoint(220.9251, 79.4276);
//        MyPoint lp4 = new MyPoint(190.7891, 26.5823);
//
//        MyPoint lp5 = new MyPoint(156.7140, 109.8025);
//        MyPoint lp6 = new MyPoint(203.7629, 100.5853);
//        MyPoint lp7 = new MyPoint(174.8570, 122.0921);
//        MyPoint lp8 = new MyPoint(216.3708, 122.3993);
//
//        MyLine ll1 = new MyLine(lp1, lp2);
//        MyLine ll2 = new MyLine(lp3, lp4);
//        MyLine ll3 = new MyLine(lp5, lp6);
//        MyLine ll4 = new MyLine(lp7, lp8);
//        int i = graphInterface.lineWithLine(ll1, ll2);
//        int i5 = graphInterface.lineWithLine(ll3, ll4);
//        int i6 = graphInterface.lineWithLine(jl1, jl2);
//        System.out.println(i);
//        System.out.println(i5);
//        System.out.println(i6);

    }

}
