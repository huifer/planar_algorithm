package com.huifer.planar.aset.entity;


import org.junit.Test;

public class MyPointTest {

    @Test
    public void testRotate() {
        MyPoint p = new MyPoint(1, 1);
        MyPoint myPoint = p.rotatePoint(new MyPoint(0, 0), 30);
    }

}
