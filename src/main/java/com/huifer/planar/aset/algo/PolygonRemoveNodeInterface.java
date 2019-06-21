package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.entity.PolygonRemoveNodeResult;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

/**
 * 描述: 面上删除节点值
 *
 * @author huifer
 * @date 2019-02-13
 */
public interface PolygonRemoveNodeInterface {

    /**
     * 面上删除节点，根据节点间距
     *
     * @param geometryList polygon list
     * @param tolerance 容差
     * @return {@link Geometry} 删除点列表
     * @throws Exception 异常
     */
    PolygonRemoveNodeResult polygonRemoveNodeByLength(List<Polygon> geometryList, double tolerance) throws Exception;

}
