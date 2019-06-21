package com.huifer.planar.aset.algo;


import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.MyPolygon;
import com.huifer.planar.aset.entity.OvlayerEnum;

/**
 * <p>Title : GraphInterface </p>
 * <p>Description : 点与线的关系</p>
 *
 * @author huifer
 * @date 2018/12/12
 */
public interface GraphInterface {

    /**
     * 右手法则计算 线与点的关系
     *
     * @param point point
     * @param polyline polyline
     * @return {@link com.huifer.planar.aset.entity.OvlayerEnum}
     */
    OvlayerEnum rightHandRule(MyPoint point, MyLine polyline);

    /**
     * 计算 两条线的交点
     *
     * @param linea 直线1
     * @param lineb 直线2
     * @return {@link com.huifer.planar.aset.entity.MyPoint}
     */
    MyPoint getIntersectionPoint(MyLine linea, MyLine lineb);

    /**
     * 点和面的关系
     *
     * @param point point
     * @param polygon polygon
     * @return {@link com.huifer.planar.aset.entity.OvlayerEnum}
     */
    OvlayerEnum pointWithPolygon(MyPoint point, MyPolygon polygon);

    /**
     * 线是否相交
     *
     * @param linea 直线1
     * @param lineb 直线2
     * @return int
     */
    int lineWithLine(MyLine linea, MyLine lineb);


}
