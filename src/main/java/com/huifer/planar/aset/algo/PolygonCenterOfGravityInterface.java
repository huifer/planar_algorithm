package com.huifer.planar.aset.algo;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : PolygonCenterOfGravityInterface </p>
 * <p>Description : 多边形重心</p>
 *
 * @author huifer
 * @date 2019-02-21
 */
public interface PolygonCenterOfGravityInterface {

    /**
     * 计算凸多边形重心
     *
     * @param covenPolygon 多边形
     * @return 重心点
     */
    Point calcGravityPointOfCovenPolygon(Polygon covenPolygon);


    /**
     * 计算凹多边形中心
     * @param concavePolygon 凹多边形
     * @return 重心点
     */
    Point calcGravityPointOfConcavePolygon(Polygon concavePolygon);

}
