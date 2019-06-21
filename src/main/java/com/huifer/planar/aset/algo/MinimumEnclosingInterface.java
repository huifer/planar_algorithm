package com.huifer.planar.aset.algo;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : MinimumEnclosingInterface </p>
 * <p>Description : 最小外接矩形</p>
 *
 * @author huifer
 * @date 2019-02-25
 */
public interface MinimumEnclosingInterface {

    /**
     * 最小外接矩形
     *
     * @param polygon 需要计算的面
     * @return 最小外接矩形结果
     */
    Polygon getMinRect(Geometry polygon);

    /**
     * 最大内接圆
     *
     * @param pg 面
     * @return 最大外接圆圆心
     */
    Point getInCirclePoint(Polygon pg);

    /**
     * 最大内接圆
     *
     * @param pg 面
     * @return 最大外接圆
     */
    Polygon getInCirclePolygon(Polygon pg);


    /**
     * 最小外接圆
     *
     * @param pg 面
     * @return 最小外接圆
     */
    Polygon getMinimumCircumscribedCircle(Polygon pg);


}
