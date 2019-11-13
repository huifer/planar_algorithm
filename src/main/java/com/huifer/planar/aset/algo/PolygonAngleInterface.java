package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.entity.PolygonAngleResult;
import org.locationtech.jts.io.ParseException;

/**
 * <p>Title : PolygonAngleInterface </p>
 * <p>Description : 面角度计算</p>
 *
 * @author huifer
 * @date 2019-01-28
 */
public interface PolygonAngleInterface {


    /**
     * polygon每一个线段角度计算
     *
     * @param wkt polygon WKT描述
     * @return {@link PolygonAngleResult}
     * @throws ParseException 异常
     */
    PolygonAngleResult polygonWktAngle(String wkt) throws ParseException;


    /**
     * 删除输入角度的值
     *
     * @param wkt   polygon WKT描述
     * @param angle 角度
     * @return {@link PolygonAngleResult}
     * @throws ParseException
     */
    PolygonAngleResult polygonWktAngleMerge(String wkt, Double angle) throws ParseException;


}
