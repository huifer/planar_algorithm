package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.entity.*;

/**
 * RailInterface
 * @author huifer
 */
public interface RailInterface {

    /**
     * 点是否在在面内
     *
     * @param point point
     * @param polygon polygon
     * @return boolean
     */
    boolean isInPolygon(MyPoint point, MyPolygon polygon);
}
