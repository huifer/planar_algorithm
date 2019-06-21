package com.huifer.planar.aset.factory;

import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;

/**
 * <p>Title : MyGeoFactory </p>
 * <p>Description : geo工厂</p>
 *
 * @author huifer
 * @date 2018/12/14
 */
public class MyGeoFactory {

    public MyPoint createPoint(double x, double y) {
        return new MyPoint(x, y);
    }

    public MyLine createLine(MyPoint p1, MyPoint p2) {
        return new MyLine(p1, p2);
    }

}
